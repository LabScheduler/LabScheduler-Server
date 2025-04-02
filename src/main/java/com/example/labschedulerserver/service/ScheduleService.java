package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;

import java.util.List;

public interface ScheduleService {

    List<Schedule> allocateSchedule(Long courseId);

    List<Schedule> getAllScheduleInSemester();

    List<Schedule> getAllScheduleBySemesterId(Long semesterId);

    List<Schedule> getAllScheduleByClassId(Long classId);

    List<Schedule> getAllScheduleByCourseId(Long courseId);

    List<Schedule> getAllScheduleByLecturerId(Long lecturerId);

    List<Schedule> createSchedule(CreateScheduleRequest request);

    Schedule cancelSchedule(Long scheduleId);

    List<Schedule> getAllSchedulesInSpecificWeek(Long weekId);
}
