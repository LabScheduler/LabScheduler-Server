package com.example.labschedulerserver.payload.request;

import lombok.Data;

@Data
public class UpdateScheduleRequest {
    private Long roomId;

    private Long lecturerId;

    private Long semesterWeekId;

    private byte dayOfWeek;

    private byte startPeriod;

    private byte totalPeriod;
}
