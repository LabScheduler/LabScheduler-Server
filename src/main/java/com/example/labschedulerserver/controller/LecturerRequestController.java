package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.common.RequestType;
import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.LecturerRequestLog;
import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.model.SemesterWeek;
import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.LecturerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/lecturer-request")
@RequiredArgsConstructor
public class LecturerRequestController {
    private final LecturerRequestService lecturerRequestService;

    @PostMapping("/create")
    public ResponseEntity<?> createScheduleRequest(@RequestBody LecturerScheduleRequest request) {
        try {
            LecturerRequest createdRequest = lecturerRequestService.createScheduleRequest(request);
            DataResponse response = DataResponse.builder()
                    .data(createdRequest)
                    .success(true)
                    .message("Schedule request created successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to create schedule request: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingRequests() {
        List<LecturerRequest> requests = lecturerRequestService.getAllPendingRequests();
        DataResponse response = DataResponse.builder()
                .data(requests)
                .success(true)
                .message("Retrieved all pending requests successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        List<LecturerRequest> requests = lecturerRequestService.getAllRequests();
        DataResponse response = DataResponse.builder()
                .data(requests)
                .success(true)
                .message("Retrieved all requests successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<?> getRequestsByLecturer(@PathVariable Long lecturerId) {
        try {
            List<LecturerRequest> requests = lecturerRequestService.getRequestsByLecturerId(lecturerId);
            DataResponse response = DataResponse.builder()
                    .data(requests)
                    .success(true)
                    .message("Retrieved lecturer requests successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to retrieve lecturer requests: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<?> getRequestById(@PathVariable Long requestId) {
        try {
            LecturerRequest request = lecturerRequestService.getRequestById(requestId);
            DataResponse response = DataResponse.builder()
                    .data(request)
                    .success(true)
                    .message("Retrieved request details successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to retrieve request: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{requestId}/log")
    public ResponseEntity<?> getRequestLog(@PathVariable Long requestId) {
        try {
            LecturerRequestLog log = lecturerRequestService.getRequestLog(requestId);
            DataResponse response = DataResponse.builder()
                    .data(log)
                    .success(true)
                    .message("Retrieved request log successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to retrieve request log: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{requestId}/process")
    public ResponseEntity<?> processRequest(@PathVariable Long requestId, @RequestBody ProcessRequest processRequest) {
        try {
            processRequest.setRequestId(requestId);
            LecturerRequestLog log = lecturerRequestService.processRequest(processRequest);
            DataResponse response = DataResponse.builder()
                    .data(log)
                    .success(true)
                    .message("Request processed successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to process request: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{requestId}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable Long requestId) {
        try {
            lecturerRequestService.cancelRequest(requestId);
            DataResponse response = DataResponse.builder()
                    .success(true)
                    .message("Request cancelled successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to cancel request: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }
} 