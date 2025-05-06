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

import java.sql.Timestamp;
import java.time.LocalDate;
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
    public List<ScheduleResponse> allocateSchedule(List<Long> courseIds) {
        List<Course> courses = courseIds.stream()
                .map(courseId -> {
                    Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
                    course.setId(courseId);
                    return course;
                })
                .toList();

        // Get all available resources
        List<Room> availableRooms = roomRepository.findAll().stream()
                .filter(room -> room.getStatus().toString().equals("AVAILABLE"))
                .toList();
        List<Schedule> existingSchedules = scheduleRepository.findAll();

        // Sort courses by priority (larger classes first)
        List<Course> sortedCourses = courses.stream()
                .sorted(Comparator.comparing(Course::getTotalStudents).reversed())
                .toList();

        List<Schedule> generatedSchedules = new ArrayList<>();
        Map<Long, Set<SemesterWeek>> usedWeeksPerSubject = new HashMap<>();

        // Iterate through each course
        for (Course course : sortedCourses) {
            Subject subject = course.getSubject();
            Long subjectId = subject.getId();

            // Initialize the set of used weeks for this subject if it doesn't exist
            usedWeeksPerSubject.putIfAbsent(subjectId, new HashSet<>());

            // Get semester weeks specific to this course's semester
            List<SemesterWeek> courseSemesterWeeks = semesterWeekRepository.findBySemesterId(course.getSemester().getId());

            // Filter to only include weeks that haven't started yet
            LocalDate currentTime = LocalDate.ofEpochDay(System.currentTimeMillis());
            courseSemesterWeeks = courseSemesterWeeks.stream()
                    .filter(week -> week.getStartDate().isAfter(currentTime))
                    // Filter out weeks already used for this subject
                    .filter(week -> !usedWeeksPerSubject.get(subjectId).contains(week))
                    .sorted(Comparator.comparing(SemesterWeek::getStartDate))
                    .toList();

            if (courseSemesterWeeks.isEmpty()) {
                throw new RuntimeException("No available semester weeks for course: " + subject.getName());
            }

            // Get lecturers specific to this course
            List<LecturerAccount> courseLecturers = course.getLecturers();

            if (courseLecturers == null || courseLecturers.isEmpty()) {
                throw new RuntimeException("No available lecturers for course: " + subject.getName());
            }

            List<CourseSection> sections = course.getCourseSections();
            if (sections == null || sections.isEmpty()) {
                throw new RuntimeException("No available sections for course: " + subject.getName());
            }

            // Get the total practice periods required from the subject
            int totalPracticePeriods = subject.getTotalPracticePeriods();
            if (totalPracticePeriods <= 0) {
                continue; // Skip if no practice periods are required
            }

            // Calculate how many sessions needed based on max periods per session
            int sessionsNeeded = (int) Math.ceil((double) totalPracticePeriods / MAX_PERIODS_PER_SESSION);

            // Iterate through each section of the course
            for (CourseSection section : sections) {
                List<Schedule> sectionSchedules = createSchedulesForSection(
                        course,
                        section,
                        availableRooms,
                        courseLecturers,
                        courseSemesterWeeks,
                        existingSchedules,
                        generatedSchedules,
                        sessionsNeeded,
                        totalPracticePeriods,
                        usedWeeksPerSubject.get(subjectId)
                );

                if (!sectionSchedules.isEmpty()) {
                    generatedSchedules.addAll(sectionSchedules);

                    // Mark the weeks as used for this subject
                    sectionSchedules.forEach(schedule ->
                            usedWeeksPerSubject.get(subjectId).add(schedule.getSemesterWeek())
                    );
                }
            }
        }

        scheduleRepository.saveAll(generatedSchedules);
        return generatedSchedules.stream().map(ScheduleMapper::mapScheduleToResponse).toList();
    }

    private List<Schedule> createSchedulesForSection(
            Course course,
            CourseSection section,
            List<Room> availableRooms,
            List<LecturerAccount> courseLecturers,
            List<SemesterWeek> courseSemesterWeeks,
            List<Schedule> existingSchedules,
            List<Schedule> generatedSchedules,
            int sessionsNeeded,
            int totalPracticePeriods,
            Set<SemesterWeek> usedWeeks
    ) {
        // Filter rooms by capacity
        List<Room> suitableRooms = availableRooms.stream()
                .filter(room -> room.getCapacity() >= section.getTotalStudentsInSection())
                .sorted(Comparator.comparing(Room::getCapacity))
                .toList();

        if (suitableRooms.isEmpty()) {
            return Collections.emptyList();
        }

        // Get all existing and newly generated schedules
        List<Schedule> allSchedules = new ArrayList<>(existingSchedules);
        allSchedules.addAll(generatedSchedules);

        List<Schedule> createdSchedules = new ArrayList<>();
        int remainingPeriods = totalPracticePeriods;

        // We need consecutive weeks for each section
        int consecutiveWeeksNeeded = Math.min(sessionsNeeded, courseSemesterWeeks.size());

        for (int i = 0; i < consecutiveWeeksNeeded && remainingPeriods > 0; i++) {
            if (i >= courseSemesterWeeks.size()) {
                break; // No more available weeks
            }

            SemesterWeek week = courseSemesterWeeks.get(i);

            // Calculate periods for this session (max 4 periods per session)
            byte periodsForThisSession = (byte) Math.min(remainingPeriods, MAX_PERIODS_PER_SESSION);

            // Find a suitable schedule for this week
            Schedule schedule = findScheduleForWeek(
                    course,
                    section,
                    suitableRooms,
                    courseLecturers,
                    week,
                    periodsForThisSession,
                    allSchedules
            );

            if (schedule != null) {
                createdSchedules.add(schedule);
                allSchedules.add(schedule); // Add to all schedules to avoid conflicts
                remainingPeriods -= periodsForThisSession;
                usedWeeks.add(week); // Mark this week as used
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
            List<Schedule> allSchedules
    ) {
        for (byte dayOfWeek = 2; dayOfWeek <= 7; dayOfWeek++) { // Monday to Saturday (2-7)
            for (byte startPeriod = 1; startPeriod <= 12; startPeriod += 2) { // Start at periods 1, 3, 5, etc.
                // Make sure the session doesn't go beyond period 15
                if (startPeriod + totalPeriod > 15) {
                    continue;
                }

                for (Room room : suitableRooms) {
                    for (LecturerAccount lecturer : courseLecturers) {
                        Schedule newSchedule = Schedule.builder()
                                .course(course)
                                .courseSection(section)
                                .room(room)
                                .lecturer(lecturer)
                                .dayOfWeek(dayOfWeek)
                                .startPeriod(startPeriod)
                                .totalPeriod(totalPeriod)
                                .semesterWeek(week)
                                .status(ScheduleStatus.IN_PROGRESS)
                                .type(ScheduleType.PRACTICE)
                                .build();

                        // Check for conflicts
                        Schedule conflict = scheduleUtils.checkScheduleConflict(newSchedule, allSchedules);
                        if (conflict == null) {
                            // No conflict, this schedule works
                            return newSchedule;
                        }
                    }
                }
            }
        }

        // Couldn't find a suitable schedule for this week
        return null;
    }


    @Override
    public List<ScheduleResponse> getScheduleByCourseId(Long courseId) {
        return scheduleRepository.findAllByCourseId(courseId).stream()
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
                .type(ScheduleType.PRACTICE)
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
        schedule.setStatus(ScheduleStatus.valueOf(request.getStatus()));

        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleResponse cancelSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        schedule.setStatus(ScheduleStatus.CANCELLED);
        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }
}
