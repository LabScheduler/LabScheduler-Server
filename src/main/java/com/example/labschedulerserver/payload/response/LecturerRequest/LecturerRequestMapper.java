package com.example.labschedulerserver.payload.response.LecturerRequest;

import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.LecturerRequestLog;
import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;

public class LecturerRequestMapper {
    public static LecturerRequestResponse toResponse(LecturerRequest request, LecturerRequestLog requestLog) {
        return LecturerRequestResponse.builder()
                .id(request.getId())
                .lecturer(request.getLecturerAccount().getFullName())
                .subject(request.getCourse().getSubject().getName())
                .groupNumber(request.getCourse().getGroupNumber())
                .sectionNumber(request.getCourseSection() != null ? request.getCourseSection().getSectionNumber() : null)
                .newRoom(request.getRoom().getName())
                .newSemesterWeek(request.getSemesterWeek() != null ? request.getSemesterWeek().getName() : null)
                .newDayOfWeek(request.getDayOfWeek())
                .newStartPeriod(request.getStartPeriod())
                .newTotalPeriod(request.getTotalPeriod())
                .lecturerBody(request.getBody())
                .managerBody(requestLog.getBody())
                .status(requestLog.getStatus().name())
                .createdAt(request.getCreatedAt())
                .build();
    }
}
