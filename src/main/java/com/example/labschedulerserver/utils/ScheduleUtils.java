package com.example.labschedulerserver.utils;

import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.model.*;

import java.util.List;

public class ScheduleUtils {
    public static final byte MIN_DAY_OF_WEEK = 2;
    public static final byte MAX_DAY_OF_WEEK = 7;
    public static final byte MIN_PERIOD = 1;
    public static final byte MAX_PERIOD = 8;
    public static final byte MORNING_START_PERIOD = 1;
    public static final byte AFTERNOON_START_PERIOD = 5;

    public static boolean isValidDayOfWeek(Byte dayOfWeek) {
        return dayOfWeek != null && dayOfWeek >= MIN_DAY_OF_WEEK && dayOfWeek <= MAX_DAY_OF_WEEK;
    }

    public static boolean isValidPeriod(Byte period) {
        return period != null && period >= MIN_PERIOD && period <= MAX_PERIOD;
    }

    public static boolean isValidTotalPeriods(Byte startPeriod, Byte totalPeriod) {
        if (startPeriod == null || totalPeriod == null || totalPeriod < MIN_PERIOD) {
            return false;
        }
        
        byte endPeriod = (byte) (startPeriod + totalPeriod - 1);
        return endPeriod <= MAX_PERIOD;
    }

    public static boolean isValidTimeSlot(Byte startPeriod, Byte totalPeriod) {
        if (!isValidPeriod(startPeriod) || !isValidTotalPeriods(startPeriod, totalPeriod)) {
            return false;
        }

        // Check if the slot is in a valid time block (morning or afternoon)
        return (startPeriod == MORNING_START_PERIOD && startPeriod + totalPeriod - 1 < AFTERNOON_START_PERIOD) ||
               (startPeriod == AFTERNOON_START_PERIOD && startPeriod + totalPeriod - 1 <= MAX_PERIOD);
    }

    public static boolean hasScheduleConflict(
            List<Schedule> existingSchedules,
            SemesterWeek week,
            Byte dayOfWeek,
            Byte startPeriod,
            Byte totalPeriod,
            Room room,
            Course course,
            Long excludeScheduleId) {
        
        if (!isValidTimeSlot(startPeriod, totalPeriod)) {
            return true;
        }

        // Check for multiple practice sessions in the same week for the same course section
        if (hasMultiplePracticeSessionsInWeek(existingSchedules, week, course, excludeScheduleId)) {
            return true;
        }

        byte endPeriod = (byte) (startPeriod + totalPeriod - 1);
        
        return existingSchedules.stream()
                .filter(schedule -> schedule.getScheduleStatus() == ScheduleStatus.IN_PROGRESS)
                .filter(schedule -> excludeScheduleId == null || !schedule.getId().equals(excludeScheduleId))
                .filter(schedule -> schedule.getSemesterWeek().getId().equals(week.getId()) &&
                                  schedule.getDayOfWeek().equals(dayOfWeek))
                .anyMatch(schedule -> {
                    byte scheduleEndPeriod = (byte) (schedule.getStartPeriod() + schedule.getTotalPeriod() - 1);
                    
                    // Check time overlap
                    boolean timeOverlap = (schedule.getStartPeriod() <= endPeriod && 
                                         scheduleEndPeriod >= startPeriod);
                    
                    if (!timeOverlap) {
                        return false;
                    }
                    
                    // Check resource conflicts
                    return hasResourceConflict(schedule, room, course);
                });
    }

    private static boolean hasMultiplePracticeSessionsInWeek(
            List<Schedule> existingSchedules,
            SemesterWeek week,
            Course course,
            Long excludeScheduleId) {
        
        return existingSchedules.stream()
                .filter(schedule -> schedule.getScheduleStatus() == ScheduleStatus.IN_PROGRESS)
                .filter(schedule -> excludeScheduleId == null || !schedule.getId().equals(excludeScheduleId))
                .filter(schedule -> schedule.getSemesterWeek().getId().equals(week.getId()))
                .filter(schedule -> schedule.getScheduleType() == ScheduleType.PRACTICE)
                .filter(schedule -> schedule.getCourseSection() != null && 
                                  schedule.getCourseSection().getCourse().getId().equals(course.getId()))
                .count() > 0;
    }

    private static boolean hasResourceConflict(Schedule existingSchedule, Room newRoom, Course newCourse) {
        // Room conflict
        if (existingSchedule.getRoom().getId().equals(newRoom.getId())) {
            return true;
        }

        // Class conflict
        if (newCourse != null && 
            existingSchedule.getCourseSection().getCourse().getClazz() != null && 
            newCourse.getClazz() != null &&
            existingSchedule.getCourseSection().getCourse().getClazz().getId()
                .equals(newCourse.getClazz().getId())) {
            return true;
        }

        // Lecturer conflict
        if (newCourse != null && 
            existingSchedule.getCourseSection().getCourse().getLecturerAccount() != null && 
            newCourse.getLecturerAccount() != null &&
            existingSchedule.getCourseSection().getCourse().getLecturerAccount().getAccountId()
                .equals(newCourse.getLecturerAccount().getAccountId())) {
            return true;
        }

        return false;
    }
} 