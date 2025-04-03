package com.example.labschedulerserver.service;

import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;

import java.util.List;

public interface ScheduleService {

    List<ScheduleResponse> allocateSchedule(Long courseId);

    List<ScheduleResponse> getAllScheduleInSemester();

    List<ScheduleResponse> getAllScheduleBySemesterId(Long semesterId);

    List<ScheduleResponse> getAllScheduleByClassId(Long classId);

    List<ScheduleResponse> getAllScheduleByCourseId(Long courseId);

    List<ScheduleResponse> getAllScheduleByLecturerId(Long lecturerId);

    ScheduleResponse createSchedule(CreateScheduleRequest request);

    ScheduleResponse cancelSchedule(Long scheduleId);

    List<ScheduleResponse> getAllSchedulesInSpecificWeek(Long weekId);
}
