package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.Schedule;

import java.util.List;

public interface ScheduleService {
    public List<Schedule> getAllScheduleInSemester();
    
    public List<Schedule> getAllScheduleBySemesterId(Long semesterId);

    public List<Schedule> getAllScheduleByClassId(Long classId);

    public List<Schedule> getAllScheduleByCourseId(Long courseId);

    public List<Schedule> getAllScheduleByLecturerId(Long lecturerId);


    public List<Schedule> getAllSchedulesInSpecificWeek(Integer weekId);

    public List<Schedule> allocateSchedule(Long courseId);

}
