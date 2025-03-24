package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.ManagerRequest;
import com.example.labschedulerserver.model.ManagerRequestLog;
import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;

public interface ManagerRequestService {
    public Schedule addSchedule(ManagerRequest request);
    public Schedule reSchedule(ManagerRequest request);
}
