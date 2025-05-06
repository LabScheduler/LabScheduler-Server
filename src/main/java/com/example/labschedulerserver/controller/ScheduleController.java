package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.UpdateScheduleRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/allocate")
    public ResponseEntity<?> allocateSchedule(@RequestParam List<Long> courseIds) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule allocated successfully")
                .data(scheduleService.allocateSchedule(courseIds))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule created successfully")
                .data(scheduleService.createSchedule(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course")
    public ResponseEntity<?> getScheduleByCourseId(@RequestParam(value = "course_id") Long courseId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule retrieved successfully")
                .data(scheduleService.getScheduleByCourseId(courseId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lecturer")
    public ResponseEntity<?> getScheduleBySemesterIdAndLecturerId(@RequestParam(value = "semester_id") Long semesterId, @RequestParam(value = "lecturer_id") Long lecturerId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule retrieved successfully")
                .data(scheduleService.getScheduleByLecturerId(semesterId, lecturerId))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long scheduleId, @RequestBody UpdateScheduleRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule updated successfully")
                .data(scheduleService.updateSchedule(scheduleId, request))
                .build();
        return ResponseEntity.ok(response);
    }
}
