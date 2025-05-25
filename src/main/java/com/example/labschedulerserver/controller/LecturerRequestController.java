package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.LecturerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/request")
@RequiredArgsConstructor
public class LecturerRequestController {

    private final LecturerRequestService lecturerRequestService;

    @GetMapping
    public ResponseEntity<?> getAllLecturerRequests(){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Lecturer requests retrieved successfully")
                .data(lecturerRequestService.getAllRequests())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingRequests(){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Pending requests retrieved successfully")
                .data(lecturerRequestService.getAllPendingRequests())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lecturer")
    public ResponseEntity<?> getRequestsByLecturer(){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Lecturer requests retrieved successfully")
                .data(lecturerRequestService.getRequestsByLecturer())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createScheduleRequest(@RequestBody LecturerScheduleRequest request){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule request created successfully")
                .data(lecturerRequestService.createScheduleRequest(request)!=null ? "Tạo yêu cầu thành công" : "Lịch đã bị trùng tại thời điểm này")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<?> processRequest(@RequestBody ProcessRequest request){
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Request processed successfully")
                .data(lecturerRequestService.processRequest(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{requestId}")
    public ResponseEntity<?> cancelRequest(@PathVariable Long requestId){
        lecturerRequestService.cancelRequest(requestId);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Request cancelled successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkRequestConflict")
    public ResponseEntity<?> checkScheduleConflict(@RequestBody LecturerScheduleRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule conflict checked successfully")
                .data(lecturerRequestService.checkScheduleConflict(request))
                .build();
        return ResponseEntity.ok(response);
    }

}
