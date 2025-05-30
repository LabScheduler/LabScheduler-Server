package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.Report.CreateReportRequest;
import com.example.labschedulerserver.payload.request.Report.UpdateReportRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<?> getAllReport(){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Get all reports successfully")
                .data(reportService.getAllReport())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingReports(){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Get all pending reports successfully")
                .data(reportService.getPendingReports())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReportByUserId(@PathVariable Long userId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(reportService.getReportByUserId(userId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportById(@PathVariable Long reportId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(reportService.getReportById(reportId))
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody CreateReportRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(reportService.createReport(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<?> updateReport(@PathVariable Long reportId, @RequestBody UpdateReportRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(reportService.updateReport(reportId, request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{reportId}")
    public ResponseEntity<?> cancelReport(@PathVariable Long reportId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(reportService.cancelReport(reportId))
                .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable Long reportId) {
        reportService.deleteReport(reportId);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Delete report successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
