package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.model.*;
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với id: " + courseId));

        if (course.getSubject().getTotalPracticePeriods() == 0) {
            throw new RuntimeException("Môn học không có tiết thực hành, không thể phân lịch lab");
        }

        List<CourseSection> courseSections = courseSectionRepository.findAllByCourseId(courseId);
        if (courseSections.isEmpty()) {
            throw new RuntimeException("Không tìm thấy section nào cho khóa học: " + courseId);
        }

        List<CourseSection> practiceSections = courseSections.stream()
                .filter(section -> section.getSectionNumber() > 0)
                .collect(Collectors.toList());

        if (practiceSections.isEmpty()) {
            throw new RuntimeException("Không tìm thấy section thực hành nào cho khóa học: " + courseId);
        }

        List<SemesterWeek> semesterWeeks = semesterWeekRepository.findBySemesterId(course.getSemester().getId())
                .stream()
                .sorted(Comparator.comparing(SemesterWeek::getName))
                .collect(Collectors.toList());
        
        if (semesterWeeks.isEmpty()) {
            throw new RuntimeException("Không tìm thấy tuần học nào cho học kỳ: " + course.getSemester().getId());
        }

        List<Room> availableRooms = roomRepository.findAll().stream()
                .filter(room -> RoomStatus.AVAILABLE.equals(room.getStatus()))
                .filter(room -> room.getCapacity() >= getMaxStudentsInSection(practiceSections))
                .sorted(Comparator.comparing(Room::getCapacity))
                .collect(Collectors.toList());
        
        if (availableRooms.isEmpty()) {
            throw new RuntimeException("Không có phòng nào có sẵn với đủ sức chứa cho các section khóa học");
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
                    throw new RuntimeException("Không thể tìm thấy time slot phù hợp cho section: " + 
                                              section.getSectionNumber() + " sau " + sessionsAllocated + 
                                              " buổi. Không đủ tuần/slot trống để phân bổ toàn bộ " + 
                                              totalSessionsNeeded + " buổi thực hành.");
                }

                if (sessionsAllocated >= totalSessionsNeeded) {
                    break;
                }
            }
        }
        
        return createdSchedules;
    }

    @Override
    public List<Schedule> getAllScheduleInSemester(Long semesterId) {
        return scheduleRepository.findByCourseSectionCourseSemesterId(semesterId);
    }

    @Override
    public List<Schedule> getAllSchedulesInSpecificWeek(Integer weekId) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getSemesterWeek().getId().equals(Long.valueOf(weekId)))
                .collect(Collectors.toList());
    }
}
