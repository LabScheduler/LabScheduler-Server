package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.repository.CourseRepository;
import com.example.labschedulerserver.repository.CourseSectionRepository;
import com.example.labschedulerserver.repository.RoomRepository;
import com.example.labschedulerserver.repository.ScheduleRepository;
import com.example.labschedulerserver.repository.SemesterWeekRepository;
import com.example.labschedulerserver.service.ScheduleService;
import com.example.labschedulerserver.ultils.ScheduleSlot;
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
    private static final byte MORNING_START_PERIOD = 1;
    private static final byte AFTERNOON_START_PERIOD = 5;

    private ScheduleSlot findSuitableTimeSlot(List<Schedule> existingSchedules, List<Room> availableRooms, 
                                             SemesterWeek week, CourseSection section) {
        boolean sectionHasScheduleInWeek = existingSchedules.stream()
                .anyMatch(schedule -> 
                    schedule.getCourseSection().getId().equals(section.getId()) && 
                    schedule.getSemesterWeek().getId().equals(week.getId()));
                    
        if (sectionHasScheduleInWeek) {
            return null;
        }

        for (Room room : availableRooms) {
            for (byte day = 2; day <= 7; day++) {
                if (isSlotAvailable(existingSchedules, room, week, day, MORNING_START_PERIOD, PERIODS_PER_SESSION, section)) {
                    return new ScheduleSlot(room, week, day, MORNING_START_PERIOD);
                }

                if (isSlotAvailable(existingSchedules, room, week, day, AFTERNOON_START_PERIOD, PERIODS_PER_SESSION, section)) {
                    return new ScheduleSlot(room, week, day, AFTERNOON_START_PERIOD);
                }
            }
        }
        return null;
    }

    private boolean isSlotAvailable(List<Schedule> existingSchedules, Room room, SemesterWeek week,
                                    byte dayOfWeek, byte startPeriod, int totalPeriods, CourseSection section) {
        if (startPeriod != MORNING_START_PERIOD && startPeriod != AFTERNOON_START_PERIOD) {
            return false;
        }

        byte endPeriod = (byte) (startPeriod + totalPeriods - 1);
        if ((startPeriod == MORNING_START_PERIOD && endPeriod > AFTERNOON_START_PERIOD - 1) || 
            (startPeriod == AFTERNOON_START_PERIOD && endPeriod > 8)) {
            return false;
        }

        boolean sectionHasScheduleInWeek = existingSchedules.stream()
                .anyMatch(schedule -> 
                    schedule.getCourseSection().getId().equals(section.getId()) && 
                    schedule.getSemesterWeek().getId().equals(week.getId()));
                    
        if (sectionHasScheduleInWeek) {
            return false;
        }

        for (Schedule schedule : existingSchedules) {
            if (!schedule.getSemesterWeek().getId().equals(week.getId()) ||
                !schedule.getDayOfWeek().equals(dayOfWeek)) {
                continue;
            }

            boolean isExistingMorning = schedule.getStartPeriod() >= MORNING_START_PERIOD && 
                                       schedule.getStartPeriod() < AFTERNOON_START_PERIOD;
            boolean isNewMorning = startPeriod == MORNING_START_PERIOD;

            if (isExistingMorning == isNewMorning) {
                if (schedule.getRoom().getId().equals(room.getId())) {
                    return false;
                }

                if (schedule.getCourseSection() != null && 
                    schedule.getCourseSection().getCourse() != null && 
                    section.getCourse() != null &&
                    schedule.getCourseSection().getCourse().getLecturerAccount() != null &&
                    section.getCourse().getLecturerAccount() != null &&
                    schedule.getCourseSection().getCourse().getLecturerAccount().getAccountId().equals(
                        section.getCourse().getLecturerAccount().getAccountId())) {
                    return false;
                }

                if (schedule.getCourseSection() != null && 
                    schedule.getCourseSection().getCourse() != null && 
                    section.getCourse() != null &&
                    schedule.getCourseSection().getCourse().getClazz() != null &&
                    section.getCourse().getClazz() != null &&
                    schedule.getCourseSection().getCourse().getClazz().getId().equals(
                        section.getCourse().getClazz().getId())) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getMaxStudentsInSection(List<CourseSection> sections) {
        return sections.stream()
                .mapToInt(CourseSection::getTotalStudentsInSection)
                .max()
                .orElse(0);
    }

    @Override
    @Transactional
    public List<Schedule> allocateSchedule(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Could not find course with id: " + courseId));

        if (course.getSubject().getTotalPracticePeriods() == 0) {
            throw new RuntimeException("Subject dont have any practice periods");
        }

        List<CourseSection> courseSections = courseSectionRepository.findAllByCourseId(courseId);
        if (courseSections.isEmpty()) {
            throw new RuntimeException("No section found with id: " + courseId);
        }

        List<CourseSection> practiceSections = courseSections.stream()
                .filter(section -> section.getSectionNumber() > 0)
                .collect(Collectors.toList());

        if (practiceSections.isEmpty()) {
            throw new RuntimeException("No practice section found with this course id: " + courseId);
        }

        List<SemesterWeek> semesterWeeks = semesterWeekRepository.findBySemesterId(course.getSemester().getId())
                .stream()
                .sorted(Comparator.comparing(SemesterWeek::getName))
                .toList();
        
        if (semesterWeeks.isEmpty()) {
            throw new RuntimeException("Could not found any semester weeks in this semester: " + course.getSemester().getId());
        }

        List<Room> availableRooms = roomRepository.findAll().stream()
                .filter(room -> RoomStatus.AVAILABLE.equals(room.getStatus()))
                .filter(room -> room.getCapacity() >= getMaxStudentsInSection(practiceSections))
                .sorted(Comparator.comparing(Room::getCapacity))
                .collect(Collectors.toList());
        
        if (availableRooms.isEmpty()) {
            throw new RuntimeException("No available room with capacity >= " + getMaxStudentsInSection(practiceSections));
        }

        List<Schedule> createdSchedules = new ArrayList<>();

        List<Schedule> existingSchedules = scheduleRepository.findAllByCurrentSemester();

        int totalPracticePeriods = course.getSubject().getTotalPracticePeriods();
        int totalSessionsNeeded = (int) Math.ceil((double) totalPracticePeriods / PERIODS_PER_SESSION);

        for (CourseSection section : practiceSections) {
            int sessionsAllocated = 0;

            while (sessionsAllocated < totalSessionsNeeded) {
                boolean foundSlot = false;
                
                for (SemesterWeek week : semesterWeeks) {
                    ScheduleSlot slot = findSuitableTimeSlot(existingSchedules, availableRooms, week, section);
                    
                    if (slot != null) {
                        Schedule schedule = Schedule.builder()
                                .courseSection(section)
                                .room(slot.getRoom())
                                .dayOfWeek(slot.getDayOfWeek())
                                .startPeriod(slot.getStartPeriod())
                                .totalPeriod(PERIODS_PER_SESSION)
                                .semesterWeek(slot.getSemesterWeek())
                                .scheduleType(ScheduleType.PRACTICE)
                                .scheduleStatus(ScheduleStatus.IN_PROGRESS)
                                .build();
                        
                        Schedule savedSchedule = scheduleRepository.save(schedule);
                        createdSchedules.add(savedSchedule);

                        existingSchedules.add(savedSchedule);
                        
                        sessionsAllocated++;
                        foundSlot = true;
                        break;
                    }
                }

                if (!foundSlot) {
                    throw new RuntimeException("Could not find suitable slot for section: " + section.getSectionNumber());
                }

                if (sessionsAllocated >= totalSessionsNeeded) {
                    break;
                }
            }
        }
        
        return createdSchedules;
    }

    @Override
    public List<Schedule> getAllScheduleInSemester() {
        return scheduleRepository.findAllByCurrentSemester();
    }

    @Override
    public List<Schedule> getAllScheduleBySemesterId(Long semesterId) {
        if (semesterId == null) {
            throw new IllegalArgumentException("Semester ID cannot be null");
        }
        return scheduleRepository.findAllBySemesterId(semesterId);
    }

    @Override
    public List<Schedule> getAllScheduleByClassId(Long classId) {
        if (classId == null) {
            throw new IllegalArgumentException("Class ID cannot be null");
        }
        return scheduleRepository.findAllByClassIdInCurrentSemester(classId);
    }

    @Override
    public List<Schedule> getAllScheduleByCourseId(Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        return scheduleRepository.findAllByCourseId(courseId);
    }

    @Override
    public List<Schedule> getAllScheduleByLecturerId(Long lecturerId) {
        if (lecturerId == null) {
            throw new IllegalArgumentException("Lecturer ID cannot be null");
        }
        return scheduleRepository.findAllByLecturerIdInCurrentSemester(lecturerId);
    }

    @Override
    public Schedule cancelSchedule(Long scheduleId) {
        if (scheduleId == null) {
            throw new IllegalArgumentException("Schedule ID cannot be null");
        }
        
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));

        if (schedule.getScheduleStatus() != ScheduleStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot cancel schedule with status: " + schedule.getScheduleStatus());
        }
        
        schedule.setScheduleStatus(ScheduleStatus.CANCELLED);
        return scheduleRepository.save(schedule);
    }


    @Override
    public List<Schedule> createSchedule(CreateScheduleRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Schedule request cannot be null");
        }

        CourseSection courseSection = courseSectionRepository.findById(request.getCourseSectionId())
                .orElseThrow(() -> new RuntimeException("Course section not found with id: " + request.getCourseSectionId()));
        
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + request.getRoomId()));
        
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room is not available: " + room.getName());
        }
        
        SemesterWeek semesterWeek = semesterWeekRepository.findById(request.getSemesterWeekId())
                .orElseThrow(() -> new RuntimeException("Semester week not found with id: " + request.getSemesterWeekId()));
        

        if (request.getDayOfWeek() < 2 || request.getDayOfWeek() > 7) {
            throw new RuntimeException("Day of week must be between 1 and 7");
        }

        if (request.getStartPeriod() < 1 || request.getStartPeriod() > 8) {
            throw new RuntimeException("Start period must be between 1 and 8");
        }
        
        if (request.getTotalPeriod() < 1 || request.getTotalPeriod() > 8) {
            throw new RuntimeException("Total period must be between 1 and 8");
        }
        
        if (request.getStartPeriod() + request.getTotalPeriod() - 1 > 8) {
            throw new RuntimeException("Schedule cannot end after period 8");
        }

        List<Schedule> existingSchedules = scheduleRepository.findAllByCurrentSemester();
        
        boolean hasConflict = existingSchedules.stream()
                .filter(schedule -> schedule.getScheduleStatus() == ScheduleStatus.IN_PROGRESS)
                .anyMatch(schedule -> 
                    schedule.getSemesterWeek().getId().equals(semesterWeek.getId()) &&
                    schedule.getDayOfWeek().equals(request.getDayOfWeek()) &&
                    ((schedule.getStartPeriod() <= request.getStartPeriod() && 
                      schedule.getStartPeriod() + schedule.getTotalPeriod() - 1 >= request.getStartPeriod()) ||
                     (schedule.getStartPeriod() <= request.getStartPeriod() + request.getTotalPeriod() - 1 && 
                      schedule.getStartPeriod() + schedule.getTotalPeriod() - 1 >= request.getStartPeriod() + request.getTotalPeriod() - 1) ||
                     (request.getStartPeriod() <= schedule.getStartPeriod() && 
                      request.getStartPeriod() + request.getTotalPeriod() - 1 >= schedule.getStartPeriod())) &&
                    (schedule.getRoom().getId().equals(room.getId()) ||
                     (schedule.getCourseSection().getCourse().getClazz().getId().equals(courseSection.getCourse().getClazz().getId()) ||
                      schedule.getCourseSection().getCourse().getLecturerAccount().getAccountId().equals(courseSection.getCourse().getLecturerAccount().getAccountId()))
                    )
                );
        
        if (hasConflict) {
            throw new RuntimeException("Schedule conflicts with existing schedules");
        }

        Schedule schedule = Schedule.builder()
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

    @Override
    public List<Schedule> getAllSchedulesInSpecificWeek(Long weekId) {
        if (weekId == null) {
            throw new IllegalArgumentException("Week ID cannot be null");
        }
        return scheduleRepository.findAllByWeekId(weekId.longValue());
    }
}
