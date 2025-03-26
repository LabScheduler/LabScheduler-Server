package com.example.labschedulerserver.ultils;

import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.model.*;

import java.util.List;

public class ScheduleUtils {

    public static boolean isValidDayOfWeek(Byte dayOfWeek) {
        return dayOfWeek != null && dayOfWeek >= 2 && dayOfWeek <= 7;
    }

    public static boolean isValidPeriod(Byte period) {
        return period != null && period >= 1 && period <= 8;
    }


    public static boolean isValidTotalPeriods(Byte startPeriod, Byte totalPeriod) {
        return startPeriod != null && totalPeriod != null && totalPeriod >= 1 && 
               (startPeriod + totalPeriod - 1) <= 8;
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
        
        return existingSchedules.stream()
                .filter(schedule -> schedule.getScheduleStatus() == ScheduleStatus.IN_PROGRESS)
                .filter(schedule -> excludeScheduleId == null || !schedule.getId().equals(excludeScheduleId))
                .anyMatch(schedule -> 
                    // Same week and day
                    schedule.getSemesterWeek().getId().equals(week.getId()) &&
                    schedule.getDayOfWeek().equals(dayOfWeek) &&
                    
                    // Time periods overlap
                    ((schedule.getStartPeriod() <= startPeriod && 
                      schedule.getStartPeriod() + schedule.getTotalPeriod() - 1 >= startPeriod) ||
                     (schedule.getStartPeriod() <= startPeriod + totalPeriod - 1 && 
                      schedule.getStartPeriod() + schedule.getTotalPeriod() - 1 >= startPeriod + totalPeriod - 1) ||
                     (startPeriod <= schedule.getStartPeriod() && 
                      startPeriod + totalPeriod - 1 >= schedule.getStartPeriod())) &&
                    
                    // Same room or same class or same lecturer
                    (schedule.getRoom().getId().equals(room.getId()) ||
                     (course != null && schedule.getCourseSection().getCourse().getClazz() != null && 
                      course.getClazz() != null &&
                      schedule.getCourseSection().getCourse().getClazz().getId().equals(course.getClazz().getId())) ||
                     (course != null && schedule.getCourseSection().getCourse().getLecturerAccount() != null && 
                      course.getLecturerAccount() != null &&
                      schedule.getCourseSection().getCourse().getLecturerAccount().getAccountId()
                        .equals(course.getLecturerAccount().getAccountId()))
                    )
                );
    }
} 