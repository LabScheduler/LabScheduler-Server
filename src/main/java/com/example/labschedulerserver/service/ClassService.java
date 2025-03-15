package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.request.AddClassRequest;

import java.util.List;
import java.util.Map;

public interface ClassService {
    public List<Clazz> getAllClasses();
    public Clazz getClassById(int id);
    public Clazz addNewClass(AddClassRequest addClassRequest);
    public void deleteClass(int id);
    public Clazz updateClass(int id, Map<String,Object> mp);
    public Clazz getClassByName(String className);
}
