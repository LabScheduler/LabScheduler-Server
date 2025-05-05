package com.example.labschedulerserver.utils;

import com.example.labschedulerserver.model.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleUtils {
    // Check for time conflict
    public boolean checkTimeConflict(Schedule schedule, Schedule newSchedule) {
        if(!(schedule.getSemesterWeek().getName().equals(newSchedule.getSemesterWeek().getName()) &&
                schedule.getDayOfWeek().equals(newSchedule.getDayOfWeek()))) {
            return false;
        }

        int start1 = schedule.getStartPeriod();
        int end1 = schedule.getStartPeriod() + schedule.getTotalPeriod();

        int start2 = newSchedule.getStartPeriod();
        int end2 = newSchedule.getStartPeriod() + newSchedule.getTotalPeriod();

        return start1 < end2 && start2 < end1;
    }

    public Schedule checkScheduleConflict(Schedule newSchedule, List<Schedule> existingSchedule) {
        return existingSchedule.stream()
                .filter(existing -> existing.getSemesterWeek().getName().equals(newSchedule.getSemesterWeek().getName()))
                .filter(existing ->{
                    if(!checkTimeConflict(existing, newSchedule)) {
                        return false;
                    }

                    boolean roomConflict = newSchedule.getRoom().equals(existing.getRoom());

                    boolean lecturerConflict = newSchedule.getLecturer().equals(existing.getLecturer());

                    return roomConflict || lecturerConflict;
                })
                .findFirst()
                .orElse(null);
    }

}
