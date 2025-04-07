package com.example.labschedulerserver.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    @JsonProperty("total_rooms")
    private long totalRooms;

    @JsonProperty("total_available_rooms")
    private long totalAvailableRooms;

    @JsonProperty("total_repairing_rooms")
    private long totalRepairingRooms;

    @JsonProperty("total_courses_in_semester")
    private long totalCoursesInSemester;

    @JsonProperty("total_practice_schedules_in_semester")
    private long totalPracticeSchedulesInSemester;

    @JsonProperty("total_students")
    private long totalStudents;

    @JsonProperty("total_lecturers")
    private long totalLecturers;
}
