package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.CreateSemesterRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentSemester() {
        DataResponse dataResponse = DataResponse.builder()
                .success(true)
                .message("Success")
                .data(semesterService.getCurrentSemester())
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/weeks")
    public ResponseEntity<?> getSemesterWeeks(@RequestParam(value = "semester_id", required = false) Long semesterId) {
        DataResponse dataResponse = DataResponse.builder()
                .success(true)
                .message("Success")
                .data(semesterService.getAllSemesterWeekInSemester(semesterId == null ? semesterService.getCurrentSemester().getId() : semesterId))
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping
    public ResponseEntity<?> createSemester(@RequestBody CreateSemesterRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .success(true)
                .message("Success")
                .data(semesterService.createSemester(request))
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
