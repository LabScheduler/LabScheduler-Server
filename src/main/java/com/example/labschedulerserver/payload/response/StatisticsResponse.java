package com.example.labschedulerserver.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsResponse {
    private long totalRooms;

    private long totalAvailableRooms;

    private long totalUnavailableRooms;

    private long totalRepairingRooms;

    private long totalCoursesInSemester;

    private long totalPracticeSchedulesInSemester;

    private long totalStudents;

    private long totalLecturers;

    private long totalPendingRequests;
}
