package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Department;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<?> getAllDepartment() {
        List<Department> departments = departmentService.getAllDepartment();
        DataResponse<?> response = DataResponse.builder()
                .data(departments)
                .success(true)
                .message("Get all departments successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        DataResponse<Department> response = DataResponse.<Department>builder()
                .data(departmentService.getDepartmentById(id))
                .success(true)
                .message("Get department by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addNewDepartment")
    public ResponseEntity<?> addNewDepartment(@RequestBody Department department) {
        DataResponse<Department> response = DataResponse.<Department>builder()
                .data(departmentService.addNewDepartment(department))
                .success(true)
                .message("Add new department successfully")
                .build();
        return ResponseEntity.ok(response);
    }


}
