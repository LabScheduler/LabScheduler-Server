package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Department;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<?> getAllDepartment() {
        List<Department> departments = departmentService.getAllDepartment();
        DataResponse response = DataResponse.builder()
                .data(departments)
                .success(true)
                .message("Get all departments successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        DataResponse response = DataResponse.<Department>builder()
                .data(departmentService.getDepartmentById(id))
                .success(true)
                .message("Get department by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewDepartment(@RequestBody Department department) {
        DataResponse response = DataResponse.<Department>builder()
                .data(departmentService.createDepartment(department))
                .success(true)
                .message("Add new department successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartmentById(id);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Delete department successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        DataResponse response = DataResponse.<Department>builder()
                .data(departmentService.updateDepartment(id, payload))
                .success(true)
                .message("Update department successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}
