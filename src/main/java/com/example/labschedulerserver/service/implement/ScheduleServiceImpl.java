package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.ScheduleService;
import com.example.labschedulerserver.utils.ScheduleSlot;
import com.example.labschedulerserver.utils.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final SemesterWeekRepository semesterWeekRepository;

    private static final byte PERIODS_PER_SESSION = 4;

    @Override
    @Transactional
    public List<Schedule> allocateSchedule(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        validateCourseForAllocation(course);

        List<CourseSection> practiceSections = getPracticeSections(courseId);
        List<SemesterWeek> semesterWeeks = getSortedSemesterWeeks(course.getSemester().getId());
        List<Room> availableRooms = getAvailableRooms(practiceSections);

        List<Schedule> existingSchedules = scheduleRepository.findAllByCurrentSemester();
        List<Schedule> createdSchedules = new ArrayList<>();

        int totalSessionsNeeded = calculateTotalSessionsNeeded(course.getSubject().getTotalPracticePeriods());

        for (CourseSection section : practiceSections) {
            allocateSessionsForSection(section, semesterWeeks, availableRooms, existingSchedules, 
                    createdSchedules, totalSessionsNeeded);
        }

        return createdSchedules;
    }

    private void validateCourseForAllocation(Course course) {
        if (course.getSubject() == null) {
            throw new BadRequestException("Course subject cannot be null");
        }
        if (course.getSubject().getTotalPracticePeriods() == 0) {
            throw new BadRequestException("Subject does not have any practice periods");
        }
        if (course.getSemester() == null) {
            throw new BadRequestException("Course semester cannot be null");
        }
    }

    private List<CourseSection> getPracticeSections(Long courseId) {
        List<CourseSection> courseSections = courseSectionRepository.findAllByCourseId(courseId);
        if (courseSections.isEmpty()) {
            throw new ResourceNotFoundException("No sections found for course with id: " + courseId);
        }

        List<CourseSection> practiceSections = courseSections.stream()
                .filter(section -> section.getSectionNumber() > 0)
                .collect(Collectors.toList());

        if (practiceSections.isEmpty()) {
            throw new BadRequestException("No practice sections found for course with id: " + courseId);
        }

        return practiceSections;
    }

    private List<SemesterWeek> getSortedSemesterWeeks(Long semesterId) {
        List<SemesterWeek> weeks = semesterWeekRepository.findBySemesterId(semesterId);
        if (weeks.isEmpty()) {
            throw new ResourceNotFoundException("No semester weeks found for semester with id: " + semesterId);
        }
        return weeks.stream()
                .sorted(Comparator.comparing(SemesterWeek::getName))
                .toList();
    }

    private List<Room> getAvailableRooms(List<CourseSection> sections) {
        int maxStudents = sections.stream()
                .mapToInt(CourseSection::getTotalStudentsInSection)
                .max()
                .orElse(0);

        if (maxStudents <= 0) {
            throw new BadRequestException("Invalid total students in section");
        }

        List<Room> rooms = roomRepository.findAll().stream()
                .filter(room -> RoomStatus.AVAILABLE.equals(room.getStatus()))
                .filter(room -> room.getCapacity() >= maxStudents)
                .sorted(Comparator.comparing(Room::getCapacity))
                .collect(Collectors.toList());

        if (rooms.isEmpty()) {
            throw new BadRequestException("No available rooms with sufficient capacity for " + maxStudents + " students");
        }

        return rooms;
    }

    private int calculateTotalSessionsNeeded(int totalPracticePeriods) {
        if (totalPracticePeriods <= 0) {
            throw new BadRequestException("Total practice periods must be greater than 0");
        }
        return (int) Math.ceil((double) totalPracticePeriods / PERIODS_PER_SESSION);
    }

    private void allocateSessionsForSection(CourseSection section, List<SemesterWeek> weeks,
                                          List<Room> availableRooms, List<Schedule> existingSchedules,
                                          List<Schedule> createdSchedules, int totalSessionsNeeded) {
        int sessionsAllocated = 0;
        Set<Long> allocatedWeeks = new HashSet<>();

        while (sessionsAllocated < totalSessionsNeeded) {
            boolean foundSlot = false;

            for (SemesterWeek week : weeks) {
                if (allocatedWeeks.contains(week.getId())) {
                    continue;
                }

                if (hasPracticeSessionInWeek(existingSchedules, week, section.getCourse())) {
                    continue;
                }

                ScheduleSlot slot = findSuitableTimeSlot(existingSchedules, availableRooms, week, section);
                if (slot != null) {
                    Schedule schedule = createScheduleFromSlot(section, slot);
                    Schedule savedSchedule = scheduleRepository.save(schedule);
                    createdSchedules.add(savedSchedule);
                    existingSchedules.add(savedSchedule);
                    allocatedWeeks.add(week.getId());
                    sessionsAllocated++;
                    foundSlot = true;
                    break;
                }
            }

            if (!foundSlot) {
                throw new BadRequestException("Could not find suitable slot for section: " + section.getSectionNumber() +
                        ". Required sessions: " + totalSessionsNeeded + ", Allocated: " + sessionsAllocated);
            }
        }
    }

    private boolean hasPracticeSessionInWeek(List<Schedule> schedules, SemesterWeek week, Course course) {
        return schedules.stream()
                .filter(schedule -> schedule.getScheduleStatus() == ScheduleStatus.IN_PROGRESS)
                .filter(schedule -> schedule.getScheduleType() == ScheduleType.PRACTICE)
                .filter(schedule -> schedule.getSemesterWeek().getId().equals(week.getId()))
                .filter(schedule -> schedule.getCourseSection() != null && 
                                  schedule.getCourseSection().getCourse().getId().equals(course.getId()))
                .count() > 0;
    }

    private ScheduleSlot findSuitableTimeSlot(List<Schedule> existingSchedules, List<Room> availableRooms,
                                            SemesterWeek week, CourseSection section) {
        for (Room room : availableRooms) {
            for (byte day = ScheduleUtils.MIN_DAY_OF_WEEK; day <= ScheduleUtils.MAX_DAY_OF_WEEK; day++) {
                if (isSlotAvailable(existingSchedules, room, week, day, ScheduleUtils.MORNING_START_PERIOD, 
                        PERIODS_PER_SESSION, section)) {
                    return new ScheduleSlot(room, week, day, ScheduleUtils.MORNING_START_PERIOD);
                }

                if (isSlotAvailable(existingSchedules, room, week, day, ScheduleUtils.AFTERNOON_START_PERIOD, 
                        PERIODS_PER_SESSION, section)) {
                    return new ScheduleSlot(room, week, day, ScheduleUtils.AFTERNOON_START_PERIOD);
                }
            }
        }
        return null;
    }

    private boolean isSlotAvailable(List<Schedule> existingSchedules, Room room, SemesterWeek week,
                                  byte dayOfWeek, byte startPeriod, int totalPeriods, CourseSection section) {
        if (!ScheduleUtils.isValidTimeSlot(startPeriod, (byte) totalPeriods)) {
            return false;
        }

        return !ScheduleUtils.hasScheduleConflict(existingSchedules, week, dayOfWeek, startPeriod, 
                (byte) totalPeriods, room, section.getCourse(), null);
    }

    private Schedule createScheduleFromSlot(CourseSection section, ScheduleSlot slot) {
        Course course = section.getCourse();
        if (course == null) {
            throw new BadRequestException("Course cannot be null");
        }

        return Schedule.builder()
                .course(course)
                .courseSection(section)
                .room(slot.getRoom())
                .dayOfWeek(slot.getDayOfWeek())
                .startPeriod(slot.getStartPeriod())
                .totalPeriod(PERIODS_PER_SESSION)
                .semesterWeek(slot.getSemesterWeek())
                .scheduleType(ScheduleType.PRACTICE)
                .scheduleStatus(ScheduleStatus.IN_PROGRESS)
                .build();
    }

    @Override
    public List<Schedule> getAllScheduleInSemester() {
        return scheduleRepository.findAllByCurrentSemester();
    }

    @Override
    public List<Schedule> getAllScheduleBySemesterId(Long semesterId) {
        if (semesterId == null) {
            throw new BadRequestException("Semester ID cannot be null");
        }
        return scheduleRepository.findAllBySemesterId(semesterId);
    }

    @Override
    public List<Schedule> getAllScheduleByClassId(Long classId) {
        if (classId == null) {
            throw new BadRequestException("Class ID cannot be null");
        }
        return scheduleRepository.findAllByClassIdInCurrentSemester(classId);
    }

    @Override
    public List<Schedule> getAllScheduleByCourseId(Long courseId) {
        if (courseId == null) {
            throw new BadRequestException("Course ID cannot be null");
        }
        return scheduleRepository.findAllByCourseId(courseId);
    }

    @Override
    public List<Schedule> getAllScheduleByLecturerId(Long lecturerId) {
        if (lecturerId == null) {
            throw new BadRequestException("Lecturer ID cannot be null");
        }
        return scheduleRepository.findAllByLecturerIdInCurrentSemester(lecturerId);
    }

    @Override
    @Transactional
    public Schedule cancelSchedule(Long scheduleId) {
        if (scheduleId == null) {
            throw new BadRequestException("Schedule ID cannot be null");
        }

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));

        if (schedule.getScheduleStatus() != ScheduleStatus.IN_PROGRESS) {
            throw new BadRequestException("Cannot cancel schedule with status: " + schedule.getScheduleStatus());
        }

        schedule.setScheduleStatus(ScheduleStatus.CANCELLED);
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public List<Schedule> createSchedule(CreateScheduleRequest request) {
        if (request == null) {
            throw new BadRequestException("Schedule request cannot be null");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        CourseSection courseSection = null;
        if (request.getType() == ScheduleType.PRACTICE) {
            if (request.getCourseSectionId() == null) {
                throw new BadRequestException("Course section ID is required for PRACTICE schedules");
            }
            courseSection = courseSectionRepository.findById(request.getCourseSectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course section not found with id: " + request.getCourseSectionId()));
            
            if (!courseSection.getCourse().getId().equals(course.getId())) {
                throw new BadRequestException("Course section does not belong to the specified course");
            }
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BadRequestException("Room is not available: " + room.getName());
        }

        SemesterWeek semesterWeek = semesterWeekRepository.findById(request.getSemesterWeekId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester week not found with id: " + request.getSemesterWeekId()));

        validateScheduleParameters(request);

        List<Schedule> existingSchedules = scheduleRepository.findAllByCurrentSemester();
        if (ScheduleUtils.hasScheduleConflict(existingSchedules, semesterWeek, request.getDayOfWeek(),
                request.getStartPeriod(), request.getTotalPeriod(), room, course, null)) {
            throw new BadRequestException("Schedule conflicts with existing schedules");
        }

        Schedule schedule = Schedule.builder()
                .course(course)
                .courseSection(courseSection)
                .room(room)
                .dayOfWeek(request.getDayOfWeek())
                .startPeriod(request.getStartPeriod())
                .totalPeriod(request.getTotalPeriod())
                .semesterWeek(semesterWeek)
                .scheduleType(request.getType())
                .scheduleStatus(ScheduleStatus.IN_PROGRESS)
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return Collections.singletonList(savedSchedule);
    }

    private void validateScheduleParameters(CreateScheduleRequest request) {
        if (request.getCourseId() == null) {
            throw new BadRequestException("Course ID cannot be null");
        }
        if (!ScheduleUtils.isValidDayOfWeek(request.getDayOfWeek())) {
            throw new BadRequestException("Invalid day of week");
        }
        if (!ScheduleUtils.isValidPeriod(request.getStartPeriod())) {
            throw new BadRequestException("Invalid start period");
        }
        if (!ScheduleUtils.isValidTotalPeriods(request.getStartPeriod(), request.getTotalPeriod())) {
            throw new BadRequestException("Invalid total periods");
        }
        if (!ScheduleUtils.isValidTimeSlot(request.getStartPeriod(), request.getTotalPeriod())) {
            throw new BadRequestException("Invalid time slot");
        }
        if (request.getType() == null) {
            throw new BadRequestException("Schedule type cannot be null");
        }
    }

    @Override
    public List<Schedule> getAllSchedulesInSpecificWeek(Long weekId) {
        if (weekId == null) {
            throw new BadRequestException("Week ID cannot be null");
        }
        return scheduleRepository.findAllByWeekId(weekId);
    }


}
