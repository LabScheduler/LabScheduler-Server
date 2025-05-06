package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.UpdateScheduleRequest;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    List<ScheduleResponse> allocateSchedule(List<Long> courseIds);

    List<ScheduleResponse> getScheduleByCourseId(Long courseId);

    List<ScheduleResponse> getScheduleByLecturerId(Long semesterId, Long lecturerId);

    List<ScheduleResponse> getScheduleByClassId(Long semesterId, Long classId);

    ScheduleResponse createSchedule(CreateScheduleRequest request);

    ScheduleResponse updateSchedule(Long scheduleId, UpdateScheduleRequest request);

    ScheduleResponse cancelSchedule(Long scheduleId);




}
