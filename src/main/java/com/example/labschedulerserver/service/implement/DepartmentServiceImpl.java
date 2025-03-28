package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Department;
import com.example.labschedulerserver.repository.DepartmentRepository;
import com.example.labschedulerserver.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;


    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(()-> new RuntimeException("Department not found"));
    }

    @Override
    public Department createDepartment(Department department) {
        if(departmentRepository.existsByName(department.getName())){
            throw new BadRequestException("Department already exists");
        }
        Department newDepartment = Department.builder().name(department.getName()).build();
        return departmentRepository.save(newDepartment);
    }

    @Override
    public void deleteDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Department not found"));
        departmentRepository.delete(department);
    }

    @Override
    public Department updateDepartment(Long id, Map<String, Object> payload) {
        Department department = departmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Department not found with id: " + id));
        department.setName((String) payload.get("name"));
        departmentRepository.save(department);
        return department;
    }
}
