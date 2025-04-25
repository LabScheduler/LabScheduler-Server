package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.payload.request.Class.CreateClassRequest;
import com.example.labschedulerserver.payload.request.Class.CreateSpecializationClass;
import com.example.labschedulerserver.payload.request.Class.UpdateClassRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/class")

public class ClassController {
    private final ClassService classService;

    @GetMapping
    public ResponseEntity<?> getAllClasses(@RequestParam(value = "class_type", required = false) String classType) {
        DataResponse response = DataResponse.builder()
                .data(classService.getAllClasses(classType))
                .message("Get all classes successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<?> getStudentsInClass(@PathVariable Long id) {
        DataResponse response = DataResponse.builder()
                .data(classService.getStudentsInClass(id))
                .message("Get students in class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{classId}/students")
    public ResponseEntity<?> addStudentsToSpecializationClass(@PathVariable Long classId, @RequestBody List<Long> studentIds) {
        classService.addStudentsToSpecializationClass(classId, studentIds);
        DataResponse response = DataResponse.builder()
                .message("Add students to specialization class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-class")
    public ResponseEntity<?> createMajorClass(@RequestBody CreateClassRequest request){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Create major class successfully")
                .data(classService.createClass(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-specialization-class")
    public ResponseEntity<?> createSpecializationClass(@RequestBody CreateSpecializationClass request){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Create specialization class successfully")
                .data(classService.createSpecializationClass(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable Long id) {
        DataResponse response = DataResponse.builder()
                .data(classService.getClassById(id))
                .message("Get class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-class/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Long id, @RequestBody UpdateClassRequest request) {
        DataResponse response = DataResponse.builder()
                .data(classService.updateClass(id, request))
                .message("Update class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-class/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        DataResponse response = DataResponse.builder()
                .message("Delete class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
