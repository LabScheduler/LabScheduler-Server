package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.LecturerRequestLog;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.LecturerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecturer-request")
@RequiredArgsConstructor
public class LecturerRequestController {
    private final LecturerRequestService lecturerRequestService;

    @PostMapping("/create")
    public ResponseEntity<?> createScheduleRequest(@RequestBody LecturerRequest request) {
        try {
            LecturerRequest savedRequest = lecturerRequestService.createScheduleRequest(request);
            DataResponse response = DataResponse.builder()
                    .data(savedRequest)
                    .success(true)
                    .message("Successfully created schedule request")
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
                .message("Successfully retrieved all pending requests")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        List<LecturerRequest> requests = lecturerRequestService.getAllRequests();
        DataResponse response = DataResponse.builder()
                .data(requests)
                .success(true)
                .message("Successfully retrieved all requests")
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
                    .message("Successfully retrieved requests for lecturer")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to retrieve requests: " + e.getMessage())
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
                    .message("Successfully retrieved request")
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
    public ResponseEntity<?> getRequestLogByRequestId(@PathVariable Long requestId) {
        try {
            LecturerRequestLog log = lecturerRequestService.getRequestLog(requestId);
            DataResponse response = DataResponse.builder()
                    .data(log)
                    .success(true)
                    .message("Successfully retrieved request log")
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
    public ResponseEntity<?> processRequest(
            @PathVariable Long requestId,
            @RequestBody ProcessRequest processRequest) {
        try {
            LecturerRequestLog log = lecturerRequestService.processRequest(processRequest);
            DataResponse response = DataResponse.builder()
                    .data(log)
                    .success(true)
                    .message("Successfully processed request")
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
                    .message("Successfully cancelled request")
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