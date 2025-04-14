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
                .courseSection(schedule.getCourseSection().getSectionNumber())
                .room(schedule.getRoom().getName())
                .dayOfWeek(schedule.getDayOfWeek())
                .startPeriod(schedule.getStartPeriod())
                .totalPeriod(schedule.getTotalPeriod())
                .lecturer(schedule.getCourse().getLecturerAccount().getFullName())
                .type(schedule.getScheduleType().name())
                .semesterWeek(schedule.getSemesterWeek().getName())
                .studyDate( schedule.getStudyDate())
                .status(schedule.getScheduleStatus().name())
                .build();
    }
}
