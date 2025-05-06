package com.example.labschedulerserver.exception;

import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;
import lombok.Getter;

@Getter
public class ScheduleException extends RuntimeException {
    private final ScheduleResponse schedule;

    public ScheduleException(String message, ScheduleResponse schedule) {
        super(message);
        this.schedule = schedule;
    }
}
