package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    public List<Department> getAllDepartment();
    public Department getDepartmentById(Integer id);
    public Department addNewDepartment(Department department);
    public void deleteDepartmentById(Integer id);
    public Department updateDepartment(Integer id, Map<String, Object> payload);
}
