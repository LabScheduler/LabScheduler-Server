package com.example.labschedulerserver.controller;


import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Subject;

import com.example.labschedulerserver.payload.request.AddSubjectRequest;
import com.example.labschedulerserver.payload.response.DataResponse;

import com.example.labschedulerserver.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subject")

public class SubjectController {
    private final SubjectService subjectService;
    @GetMapping
    public ResponseEntity<DataResponse<?>> GetAllSubjects() {
        List<Subject> classes = subjectService.getAllSubjects();
        DataResponse<?> response = DataResponse.builder()
                .data(classes)
                .success(true)
                .message("Get all subjects successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        DataResponse<Subject> response = DataResponse.<Subject>builder()
                .data(subjectService.getSubjectById(id))
                .success(true)
                .message("Get subject by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<?> createNewSubject(@RequestBody AddSubjectRequest request) {
        DataResponse<Subject> response = DataResponse.<Subject>builder()
                .data(subjectService.addNewSubject(request))
                .success(true)
                .message("Add new class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Integer id) {
        subjectService.deleteSubject(id);
        DataResponse<?> response = DataResponse.builder()
                .success(true)
                .message("Delete class successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateSubjectById(@PathVariable Integer id,@RequestBody Map<String, Object> mp) {
        DataResponse<?> response = DataResponse.builder()
                .success(true)
                .data(subjectService.updateSubject(id,mp))
                .message("Update subject successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/find/{name}")
    public ResponseEntity<?> findSubjectByName(@PathVariable String name) {
        DataResponse<Subject> response = DataResponse.<Subject>builder()
                .data(subjectService.getSubjectByName(name))
                .success(true)
                .message("Get subject by name successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
