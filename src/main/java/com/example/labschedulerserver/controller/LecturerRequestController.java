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
import lombok.Data;
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
        DataResponse dataResponse = DataResponse.builder()
                .data(lecturerRequestService.createScheduleRequest(request))
                .success(true)
                .message("Request created successfully")
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/get/pending")
    public ResponseEntity<?> getAllPendingRequests() {
        DataResponse response = DataResponse.builder()
                .data(lecturerRequestService.getAllPendingRequests())
                .success(true)
                .message("Retrieved all pending requests successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllRequests() {
        DataResponse response = DataResponse.builder()
                .data(lecturerRequestService.getAllRequests())
                .success(true)
                .message("Retrieved all requests successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/lecturer/{lecturerId}")
    public ResponseEntity<?> getRequestsByLecturer(@PathVariable Long lecturerId) {
        DataResponse response = DataResponse.builder()
                .data(lecturerRequestService.getRequestsByLecturerId(lecturerId))
                .success(true)
                .message("Retrieved requests for lecturer successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{requestId}")
    public ResponseEntity<?> getRequestById(@PathVariable Long requestId) {
        DataResponse response = DataResponse.builder()
                .data(lecturerRequestService.getRequestById(requestId))
                .success(true)
                .message("Retrieved request by ID successfully")
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping("/process-request")
    public ResponseEntity<?> processRequest(@RequestBody ProcessRequest processRequest) {
       DataResponse response = DataResponse.builder()
                .data(lecturerRequestService.processRequest(processRequest))
                .success(true)
                .message("Request processed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelRequest(@RequestParam(name = "request_id") Long requestId) {
        lecturerRequestService.cancelRequest(requestId);
        DataResponse dataResponse = DataResponse.builder()
                .success(true)
                .message("Request cancelled successfully")
                .build();
        return ResponseEntity.ok(dataResponse);
    }

} 