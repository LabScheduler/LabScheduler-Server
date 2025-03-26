package com.example.labschedulerserver.ultils;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.Schedule;

public class RequestUtils {

    public static boolean isLecturerTeachingCourse(Course course, LecturerAccount lecturer) {
        if (course == null || lecturer == null || course.getLecturerAccount() == null) {
            return false;
        }
        
        return course.getLecturerAccount().getAccountId().equals(lecturer.getAccountId());
    }

    public static boolean isLecturerTeachingSchedule(Schedule schedule, LecturerAccount lecturer) {
        if (schedule == null || lecturer == null || 
            schedule.getCourseSection() == null || 
            schedule.getCourseSection().getCourse() == null ||
            schedule.getCourseSection().getCourse().getLecturerAccount() == null) {
            return false;
        }
        
        return schedule.getCourseSection().getCourse().getLecturerAccount().getAccountId()
                .equals(lecturer.getAccountId());
    }

    public static boolean hasValidReason(LecturerRequest request) {
        return request.getReason() != null && !request.getReason().trim().isEmpty();
    }

    public static boolean hasRescheduleChanges(LecturerRequest request) {
        return request.getNewRoom() != null || 
               request.getNewSemesterWeek() != null || 
               request.getNewDayOfWeek() != null || 
               request.getNewStartPeriod() != null ||
               request.getNewTotalPeriod() != null;
    }
} 