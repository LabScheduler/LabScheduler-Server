package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateScheduleRequest {
    @JsonProperty("schedule_id")
    private Long scheduleId;
    @JsonProperty("room_id")
    private Long roomId;
    private String status;
}
