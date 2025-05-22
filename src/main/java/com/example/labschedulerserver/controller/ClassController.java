package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.Class.CreateClassRequest;
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
    public ResponseEntity<?> getAllClasses(@RequestParam(required = false) String classType) {
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
    public ResponseEntity<?> addStudentToClass(@PathVariable Long classId, @RequestBody List<Long> studentIds) {
        classService.addStudentToClass(classId, studentIds);
        DataResponse response = DataResponse.builder()
                .message("Add students to specialization class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }



    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody CreateClassRequest request){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Create major class successfully")
                .data(classService.createClass(request))
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Long id, @RequestBody UpdateClassRequest request) {
        DataResponse response = DataResponse.builder()
                .data(classService.updateClass(id, request))
                .message("Update class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        DataResponse response = DataResponse.builder()
                .message("Delete class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{classId}/students")
    public ResponseEntity<?> deleteStudentFromClass(@PathVariable Long classId, @RequestBody Long studentId) {
        classService.deleteStudentFromClass(classId, studentId);
        DataResponse response = DataResponse.builder()
                .message("Delete students from class successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
