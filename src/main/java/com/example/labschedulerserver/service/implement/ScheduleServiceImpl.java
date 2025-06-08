package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.exception.ScheduleException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.UpdateScheduleRequest;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleMapper;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.ScheduleService;
import com.example.labschedulerserver.utils.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    // Maximum periods per lab session
    private static final byte MAX_PERIODS_PER_SESSION = 4;
    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final LecturerAccountRepository lecturerRepository;
    private final SemesterWeekRepository semesterWeekRepository;
    private final ScheduleUtils scheduleUtils;
    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;

    @Override
    public List<ScheduleResponse> allocateSchedule(Long courseId, Long startWeekId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        List<Room> availableRooms = roomRepository.findAll().stream()
                .filter(room -> room.getStatus().toString().equals("AVAILABLE"))
                .toList();
        List<Schedule> existingSchedules = scheduleRepository.findAll();
        List<Schedule> generatedSchedules = new ArrayList<>();

        Subject subject = course.getSubject();
        
        // Get semester weeks
        List<SemesterWeek> courseSemesterWeeks = semesterWeekRepository.findBySemesterId(course.getSemester().getId());
        SemesterWeek startingWeek = semesterWeekRepository.findById(startWeekId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester week not found with id: " + startWeekId));

        courseSemesterWeeks = courseSemesterWeeks.stream()
                .filter(week -> !week.getStartDate().isBefore(startingWeek.getStartDate()))
                .sorted(Comparator.comparing(SemesterWeek::getStartDate))
                .toList();

        if (courseSemesterWeeks.isEmpty()) {
            throw new RuntimeException("No available semester weeks after the specified starting week");
        }

        List<LecturerAccount> courseLecturers = course.getLecturers();
        if (courseLecturers == null || courseLecturers.isEmpty()) {
            throw new RuntimeException("No available lecturers for course: " + subject.getName());
        }

        List<Room> availableTheoryRooms = availableRooms.stream()
                .filter(room -> room.getType().toString().equals("LECTURE_HALL"))
                .toList();
        List<Room> availablePracticeRooms = availableRooms.stream()
                .filter(room -> room.getType().toString().equals("COMPUTER_LAB"))
                .toList();

        // generate theory schedules
        int totalTheoryPeriods = subject.getTotalTheoryPeriods();
        if (totalTheoryPeriods > 0) {
            int theorySessionsNeeded = (int) Math.ceil((double) totalTheoryPeriods / MAX_PERIODS_PER_SESSION);

            List<Schedule> theorySchedules = createTheorySchedules(
                    course,
                    availableTheoryRooms,
                    courseLecturers,
                    courseSemesterWeeks,
                    existingSchedules,
                    generatedSchedules,
                    theorySessionsNeeded,
                    totalTheoryPeriods
            );
            generatedSchedules.addAll(theorySchedules);
            System.out.println("Generated theory schedules: " + theorySchedules.size());
        }

        // generate practice schedules
        int totalPracticePeriods = subject.getTotalPracticePeriods();
        if (totalPracticePeriods > 0) {
            List<CourseSection> sections = course.getCourseSections();
            if (sections == null || sections.isEmpty()) {
                throw new RuntimeException("No available sections for practice sessions");
            }

            int totalWeeks = courseSemesterWeeks.size();
            int practiceStartWeekIndex = totalWeeks / 3;
            
            List<SemesterWeek> practiceWeeks = courseSemesterWeeks.subList(
                    practiceStartWeekIndex,
                    courseSemesterWeeks.size()
            );

            int sessionsPerSection = (int) Math.ceil((double) totalPracticePeriods / MAX_PERIODS_PER_SESSION);
            Set<SemesterWeek> usedPracticeWeeks = new HashSet<>();

            for (CourseSection section : sections) {
                List<Schedule> sectionSchedules = createPracticeSchedulesForSection(
                        course,
                        section,
                        availablePracticeRooms,
                        courseLecturers,
                        practiceWeeks,
                        existingSchedules,
                        generatedSchedules,
                        sessionsPerSection,
                        totalPracticePeriods,
                        usedPracticeWeeks
                );
                generatedSchedules.addAll(sectionSchedules);
            }
        }

        scheduleRepository.saveAll(generatedSchedules);
        return generatedSchedules.stream().map(ScheduleMapper::mapScheduleToResponse).toList();
    }

    private List<Schedule> createTheorySchedules(
            Course course,
            List<Room> availableRooms,
            List<LecturerAccount> courseLecturers,
            List<SemesterWeek> weeks,
            List<Schedule> existingSchedules,
            List<Schedule> generatedSchedules,
            int sessionsNeeded,
            int totalPeriods
    ) {
        List<Schedule> createdSchedules = new ArrayList<>();
        List<Schedule> allSchedules = new ArrayList<>(existingSchedules);
        allSchedules.addAll(generatedSchedules);
        int remainingPeriods = totalPeriods;

        // Lọc phòng theo capacity cho cả lớp
        List<Room> suitableRooms = availableRooms.stream()
                .filter(room -> room.getCapacity() >= course.getMaxStudents())
                .sorted(Comparator.comparing(Room::getCapacity))
                .toList();

        if (suitableRooms.isEmpty()) {
            return Collections.emptyList();
        }

        for (int i = 0; remainingPeriods > 0; i++) {
            SemesterWeek week = weeks.get(i);
            byte periodsForThisSession = (byte) Math.min(remainingPeriods, MAX_PERIODS_PER_SESSION);

            Schedule schedule = findScheduleForWeek(
                    course,
                    null, // No section for theory classes
                    suitableRooms,
                    courseLecturers,
                    week,
                    periodsForThisSession,
                    allSchedules,
                    ScheduleType.THEORY
            );

            if (schedule != null) {
                createdSchedules.add(schedule);
                allSchedules.add(schedule);
                remainingPeriods -= periodsForThisSession;
            }
        }

        return createdSchedules;
    }

    private List<Schedule> createPracticeSchedulesForSection(
            Course course,
            CourseSection section,
            List<Room> availableRooms,
            List<LecturerAccount> courseLecturers,
            List<SemesterWeek> practiceWeeks,
            List<Schedule> existingSchedules,
            List<Schedule> generatedSchedules,
            int sessionsNeeded,
            int periodsPerSection,
            Set<SemesterWeek> usedWeeks
    ) {
        List<Room> suitableRooms = availableRooms.stream()
                .filter(room -> room.getCapacity() >= section.getMaxStudentsInSection())
                .sorted(Comparator.comparing(Room::getCapacity))
                .toList();

        if (suitableRooms.isEmpty()) {
            return Collections.emptyList();
        }

        List<Schedule> allSchedules = new ArrayList<>(existingSchedules);
        allSchedules.addAll(generatedSchedules);
        List<Schedule> createdSchedules = new ArrayList<>();
        int remainingPeriods = periodsPerSection;

        int consecutiveWeeksCount = 0;
        for (int i = 0; (consecutiveWeeksCount < sessionsNeeded) && remainingPeriods > 0; i++) {
            if (i >= practiceWeeks.size()) {
                break;
            }

            SemesterWeek week = practiceWeeks.get(i);
            if (usedWeeks.contains(week)) {
                continue;
            }

            byte periodsForThisSession = (byte) Math.min(remainingPeriods, MAX_PERIODS_PER_SESSION);
            Schedule schedule = findScheduleForWeek(
                    course,
                    section,
                    suitableRooms,
                    courseLecturers,
                    week,
                    periodsForThisSession,
                    allSchedules,
                    ScheduleType.PRACTICE
            );

            if (schedule != null) {
                createdSchedules.add(schedule);
                allSchedules.add(schedule);
                remainingPeriods -= periodsForThisSession;
                usedWeeks.add(week);
                consecutiveWeeksCount++;
            }
        }

        return createdSchedules;
    }

    private Schedule findScheduleForWeek(
            Course course,
            CourseSection section,
            List<Room> suitableRooms,
            List<LecturerAccount> courseLecturers,
            SemesterWeek week,
            byte totalPeriod,
            List<Schedule> allSchedules,
            ScheduleType type
    ) {
        for (byte dayOfWeek = 2; dayOfWeek <= 7; dayOfWeek++) {
            for (byte startPeriod = 1; startPeriod < 10; startPeriod += 1) {
                byte endPeriod = (byte) (startPeriod + totalPeriod - 1);

                if (startPeriod <= 5 && endPeriod >= 6) {
                    continue;
                }
                
                if (startPeriod + totalPeriod - 1 > 15) {
                    continue;
                }

                for (Room room : suitableRooms) {
                    for (LecturerAccount lecturer : courseLecturers) {
                        Schedule newSchedule = Schedule.builder()
                                .course(course)
                                .courseSection(section) // null for theory, section for practice
                                .room(room)
                                .lecturer(lecturer)
                                .dayOfWeek(dayOfWeek)
                                .startPeriod(startPeriod)
                                .totalPeriod(totalPeriod)
                                .semesterWeek(week)
                                .status(ScheduleStatus.IN_PROGRESS)
                                .type(type)
                                .build();

                        Schedule conflict = scheduleUtils.checkScheduleConflict(newSchedule, allSchedules);
                        if (conflict == null) {
                            return newSchedule;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<ScheduleResponse> getScheduleBySemesterId(Long semesterId) {
        return scheduleRepository.findAllBySemesterId(semesterId).stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getScheduleByCourseId(Long semesterId, Long courseId) {
        return scheduleRepository.findAllByCourseId(courseId).stream()
                .filter(schedule -> schedule.getSemesterWeek().getSemester().getId().equals(semesterId))
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getScheduleByLecturerId(Long semesterId, Long lecturerId) {
        return scheduleRepository.findAllByLecturerIdAndSemesterId(semesterId, lecturerId).stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getScheduleByClassId(Long semesterId, Long classId) {
        return scheduleRepository.findAllByClassIdAndSemesterId(classId, semesterId).stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponse createSchedule(CreateScheduleRequest request) {
        Schedule schedule = Schedule.builder()
                .course(courseRepository.findById(request.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId())))
                .courseSection(courseSectionRepository.findById(request.getCourseSectionId()).orElseThrow(() -> new ResourceNotFoundException("Course section not found with id: " + request.getCourseSectionId())))
                .room(roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId())))
                .lecturer(lecturerRepository.findById(request.getLecturerId()).orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + request.getLecturerId())))
                .semesterWeek(semesterWeekRepository.findById(request.getSemesterWeekId()).orElseThrow(() -> new ResourceNotFoundException("Semester week not found with id: " + request.getSemesterWeekId())))
                .dayOfWeek(request.getDayOfWeek())
                .startPeriod(request.getStartPeriod())
                .totalPeriod(request.getTotalPeriod())
                .status(ScheduleStatus.IN_PROGRESS)
                .build();

        List<Schedule> existingSchedules = scheduleRepository.findAllByCurrentSemester();
        Schedule conflictSchedule = scheduleUtils.checkScheduleConflict(schedule, existingSchedules);
        if (conflictSchedule != null) {
            throw new ScheduleException("New schedule has conflict", ScheduleMapper.mapScheduleToResponse(conflictSchedule));
        }
        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleResponse updateSchedule(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));

        schedule.setRoom(roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId())));
        schedule.setLecturer(lecturerRepository.findById(request.getLecturerId()).orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + request.getLecturerId())));
        schedule.setSemesterWeek(semesterWeekRepository.findById(request.getSemesterWeekId()).orElseThrow(() -> new ResourceNotFoundException("Semester week not found with id: " + request.getSemesterWeekId())));
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartPeriod(request.getStartPeriod());
        schedule.setTotalPeriod(request.getTotalPeriod());

        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleResponse cancelSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        schedule.setStatus(ScheduleStatus.CANCELLED);
        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        scheduleRepository.delete(schedule);
    }

    @Override
    public ScheduleResponse checkScheduleConflict(CreateScheduleRequest request) {
        Schedule schedule = Schedule.builder()
                .course(courseRepository.findById(request.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId())))
                .courseSection(courseSectionRepository.findById(request.getCourseSectionId()).orElseThrow(() -> new ResourceNotFoundException("Course section not found with id: " + request.getCourseSectionId())))
                .room(roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId())))
                .lecturer(lecturerRepository.findById(request.getLecturerId()).orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + request.getLecturerId())))
                .semesterWeek(semesterWeekRepository.findById(request.getSemesterWeekId()).orElseThrow(() -> new ResourceNotFoundException("Semester week not found with id: " + request.getSemesterWeekId())))
                .dayOfWeek(request.getDayOfWeek())
                .startPeriod(request.getStartPeriod())
                .totalPeriod(request.getTotalPeriod())
                .status(ScheduleStatus.IN_PROGRESS)
                .build();
        List<Schedule> existingSchedules = scheduleRepository.findAllByCurrentSemester();
        Schedule conflictSchedule = scheduleUtils.checkScheduleConflict(schedule, existingSchedules);
        if (conflictSchedule != null) {
            return ScheduleMapper.mapScheduleToResponse(conflictSchedule);
        }
        return null;
    }
}
