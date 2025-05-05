package com.example.labschedulerserver.service;

import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    List<ScheduleResponse> allocateSchedule();

}
