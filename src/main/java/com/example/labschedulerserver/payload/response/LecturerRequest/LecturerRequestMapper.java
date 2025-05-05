package com.example.labschedulerserver.payload.response.LecturerRequest;

import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.LecturerRequestLog;

public class LecturerRequestMapper {
    public static LecturerRequestResponse toResponse(LecturerRequest request, LecturerRequestLog requestLog) {
        return LecturerRequestResponse.builder()
                .id(request.getId())
                .lecturer(request.getLecturerAccount().getFullName())
                .subject(request.getCourse().getSubject().getName())
                .groupNumber(request.getCourse().getGroupNumber())
                .sectionNumber(request.getCourseSection().getSectionNumber())
                .newRoom(request.getRoom().getName())
                .newSemesterWeek(request.getSemesterWeek().getName())
                .newDayOfWeek(request.getDayOfWeek())
                .newStartPeriod(request.getStartPeriod())
                .newTotalPeriod(request.getTotalPeriod())
                .body(request.getBody())
                .status(requestLog.getStatus().name())
                .createdAt(request.getCreatedAt())
                .build();

    }
}
