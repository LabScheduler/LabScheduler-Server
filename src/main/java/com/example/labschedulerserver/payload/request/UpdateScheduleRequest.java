package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateScheduleRequest {
    @JsonProperty("room_id")
    private Long roomId;

    @JsonProperty("lecturer_id")
    private Long lecturerId;

    @JsonProperty("semester_week_id")
    private Long semesterWeekId;

    @JsonProperty("day_of_week")
    private byte dayOfWeek;

    @JsonProperty("start_period")
    private byte startPeriod;

    @JsonProperty("total_period")
    private byte totalPeriod;

    private String status;
}
