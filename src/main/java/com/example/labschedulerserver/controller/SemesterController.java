package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/semester")
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping
    public ResponseEntity<?> getAllSemesters() {
        DataResponse dataResponse = DataResponse.builder()
                .success(true)
                .message("Success")
                .data(semesterService.getAllSemesters())
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
