package com.example.labschedulerserver.service;

import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.UpdateScheduleRequest;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    List<ScheduleResponse> allocateSchedule(Long courseIds, Long startWeekId);

    List<ScheduleResponse> getScheduleBySemesterId(Long semesterId);

    List<ScheduleResponse> getScheduleByCourseId(Long semesterId, Long courseId);

    List<ScheduleResponse> getScheduleByLecturerId(Long semesterId, Long lecturerId);

    List<ScheduleResponse> getScheduleByClassId(Long semesterId, Long classId);

    ScheduleResponse createSchedule(CreateScheduleRequest request);

    ScheduleResponse updateSchedule(Long scheduleId, UpdateScheduleRequest request);

    ScheduleResponse cancelSchedule(Long scheduleId);

    void deleteSchedule(Long scheduleId);

    ScheduleResponse checkScheduleConflict(CreateScheduleRequest request);


}
