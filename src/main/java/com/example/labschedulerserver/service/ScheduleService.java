package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.Schedule;

import java.util.List;

public interface ScheduleService {
    public List<Schedule> createSchedule(List<Course> courses);
}
