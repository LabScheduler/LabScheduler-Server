package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.model.SemesterWeek;
import com.example.labschedulerserver.payload.request.CreateSemesterRequest;

import java.util.List;

public interface SemesterService {
    public Semester getCurrentSemester();

    public List<Semester> getAllSemesters();

//    public List<SemesterWeek> getAllSemesterWeekInSemester();

    public List<SemesterWeek> getAllSemesterWeekInSemester(Long semesterId);

    public Semester createSemester(CreateSemesterRequest request);
}
