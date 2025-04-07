package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.SumaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sumary")
public class SumaryController {
    private final SumaryService sumaryService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        var dashboard = sumaryService.getDashboard();
        DataResponse response = DataResponse.builder()
                .data(dashboard)
                .success(true)
                .message("Successfully retrieved dashboard data")
                .build();
        return ResponseEntity.ok(response);
    }
}
