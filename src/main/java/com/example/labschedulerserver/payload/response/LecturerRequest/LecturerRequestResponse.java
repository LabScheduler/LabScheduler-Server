package com.example.labschedulerserver.payload.response.LecturerRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class LecturerRequestResponse {
    private Long id;

    private String lecturer;

    private String subject;

    @JsonProperty("group_number")
    private Integer groupNumber;

    @JsonProperty("section_number")
    private Integer sectionNumber;

    @JsonProperty("new_room")
    private String newRoom;

    @JsonProperty("new_semester_week")
    private String newSemesterWeek;

    @JsonProperty("new_day_of_week")
    private Byte newDayOfWeek;

    @JsonProperty("new_start_period")
    private Byte newStartPeriod;

    @JsonProperty("new_total_period")
    private Byte newTotalPeriod;

    private String reason;

    private String type;

    private String status;

    @JsonProperty("created_at")
    private Timestamp createdAt;

}