package com.example.labschedulerserver.service;

import com.example.labschedulerserver.payload.request.Class.CreateClassRequest;
import com.example.labschedulerserver.payload.request.Class.UpdateClassRequest;
import com.example.labschedulerserver.payload.response.Class.ClassResponse;
import com.example.labschedulerserver.payload.response.User.StudentResponse;

import java.util.List;

public interface ClassService {
    ClassResponse createClass(CreateClassRequest request);

    List<ClassResponse> getAllClasses(String classType);


    ClassResponse getClassById(Long id);

    List<StudentResponse> getStudentsInClass(Long classId);

    ClassResponse updateClass(Long id, UpdateClassRequest request);

    void deleteClass(Long id);

    void deleteStudentFromClass(Long classId, Long studentId);
    void addStudentToClass(Long classId, List<Long> studentIds);
}
