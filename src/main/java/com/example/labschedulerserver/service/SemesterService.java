package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.payload.response.SemesterResponse;

import java.util.List;

public interface SemesterService {
    public Semester getCurrentSemester();

    public List<SemesterResponse> getAllSemesters();
}
