package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    public List<Department> getAllDepartment();
    public Department getDepartmentById(Long id);
    public Department createDepartment(Department department);
    public void deleteDepartmentById(Long id);
    public Department updateDepartment(Long id, Map<String, Object> payload);
}
