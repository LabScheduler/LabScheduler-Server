package com.example.labschedulerserver.service;


import com.example.labschedulerserver.model.Subject;

import com.example.labschedulerserver.payload.request.AddSubjectRequest;

import java.util.List;
import java.util.Map;


public interface SubjectService {
    public List<Subject> getAllSubjects();

    public Subject getSubjectById(int id);

    public Subject addNewSubject(AddSubjectRequest request);

    public void deleteSubject(int id);

    public Subject updateSubject(int id, Map<String, Object> mp);

    public Subject getSubjectByName(String name);
}
