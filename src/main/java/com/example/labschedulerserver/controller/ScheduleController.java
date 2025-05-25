package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.UpdateScheduleRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule created successfully")
                .data(scheduleService.createSchedule(request))
                .build();
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyAuthority('MANAGER', 'STUDENT', 'LECTURER')")
    @GetMapping
    public ResponseEntity<?> getAllSchedules(@RequestParam Long semesterId) {
        List<?> schedules = scheduleService.getScheduleBySemesterId(semesterId);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedules retrieved successfully")
                .data(schedules)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'STUDENT', 'LECTURER')")
    @GetMapping("/course")
    public ResponseEntity<?> getScheduleByCourseId(@RequestParam Long semesterId, @RequestParam Long courseId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule retrieved successfully")
                .data(scheduleService.getScheduleByCourseId(semesterId, courseId))
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER', 'STUDENT', 'LECTURER')")
    @GetMapping("/lecturer")
    public ResponseEntity<?> getScheduleBySemesterIdAndLecturerId(@RequestParam Long semesterId, @RequestParam Long lecturerId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule retrieved successfully")
                .data(scheduleService.getScheduleByLecturerId(semesterId, lecturerId))
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER', 'STUDENT', 'LECTURER')")
    @GetMapping("/class")
    public ResponseEntity<?> getScheduleBySemesterIdAndClassId(@RequestParam Long semesterId, @RequestParam Long classId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule retrieved successfully")
                .data(scheduleService.getScheduleByClassId(semesterId, classId))
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long scheduleId, @RequestBody UpdateScheduleRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule updated successfully")
                .data(scheduleService.updateSchedule(scheduleId, request))
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PatchMapping("{scheduleId}")
    public ResponseEntity<?> cancelSchedule(@PathVariable Long scheduleId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule cancelled successfully")
                .data(scheduleService.cancelSchedule(scheduleId))
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkScheduleConflict")
    public ResponseEntity<?> checkScheduleConflict(@RequestBody CreateScheduleRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Schedule conflict checked successfully")
                .data(scheduleService.checkScheduleConflict(request))
                .build();
        return ResponseEntity.ok(response);
    }
}
