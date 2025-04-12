package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.payload.request.Class.AddClassRequest;
import com.example.labschedulerserver.payload.request.Class.UpdateClassRequest;

import java.util.List;
import java.util.Map;

public interface ClassService {
    public List<Clazz> getAllClasses();
    public Clazz getClassById(Long id);
    public Clazz addNewClass(AddClassRequest addClassRequest);
    public void deleteClass(Long id);
    public Clazz updateClass(Long id, UpdateClassRequest request);
    public Clazz getClassByName(String className);

    public List<Clazz> getAllClassesByMajorId(Long majorId);
}
