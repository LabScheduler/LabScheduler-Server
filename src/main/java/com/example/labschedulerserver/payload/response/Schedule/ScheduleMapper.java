package com.example.labschedulerserver.payload.response.Schedule;

import com.example.labschedulerserver.model.Schedule;

import java.time.LocalDateTime;

public class ScheduleMapper {
    public static ScheduleResponse mapScheduleToResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .subjectCode(schedule.getCourse().getSubject().getCode())
                .subjectName(schedule.getCourse().getSubject().getName())
                .courseGroup(schedule.getCourse().getGroupNumber())
                .courseSection(schedule.getCourseSection() != null ? schedule.getCourseSection().getSectionNumber() : null)
                .room(schedule.getRoom().getName())
                .dayOfWeek(schedule.getDayOfWeek())
                .startPeriod(schedule.getStartPeriod())
                .totalPeriod(schedule.getTotalPeriod())
                .lecturer(schedule.getLecturer().getFullName())
                .semesterWeek(schedule.getSemesterWeek().getName())
                .status(schedule.getStatus().name())
                .clazz(schedule.getCourse().getClazz().getName())
                .type(schedule.getType().name())
                .build();
    }
}
