package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/allocate")
    public ResponseEntity<?> allocateSchedule(@RequestParam("course_id") Long courseId) {
        try {
            List<Schedule> schedules = scheduleService.allocateSchedule(courseId);
            DataResponse response = DataResponse.builder()
                    .data(schedules)
                    .success(true)
                    .message("Successfully allocated schedules for course")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to allocate schedules: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSchedulesInCurrentSemester() {
        List<Schedule> schedules = scheduleService.getAllScheduleInSemester();
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for current semester")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<?> getSchedulesInSemester(@PathVariable Long semesterId) {
        List<Schedule> schedules = scheduleService.getAllScheduleBySemesterId(semesterId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for semester")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getSchedulesByClass(@PathVariable Long classId) {
        List<Schedule> schedules = scheduleService.getAllScheduleByClassId(classId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for class")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getSchedulesByCourse(@PathVariable Long courseId) {
        List<Schedule> schedules = scheduleService.getAllScheduleByCourseId(courseId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for course")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<?> getSchedulesByLecturer(@PathVariable Long lecturerId) {
        List<Schedule> schedules = scheduleService.getAllScheduleByLecturerId(lecturerId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for lecturer")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/week/{weekId}")
    public ResponseEntity<?> getSchedulesInWeek(@PathVariable Long weekId) {
        List<Schedule> schedules = scheduleService.getAllSchedulesInSpecificWeek(weekId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for week")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest request) {
        try {
            List<Schedule> schedules = scheduleService.createSchedule(request);
            DataResponse response = DataResponse.builder()
                    .data(schedules)
                    .success(true)
                    .message("Successfully created schedule")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to create schedule: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/cancel/{scheduleId}")
    public ResponseEntity<?> cancelSchedule(@PathVariable Long scheduleId) {
        try {
            Schedule schedule = scheduleService.cancelSchedule(scheduleId);
            DataResponse response = DataResponse.builder()
                    .data(schedule)
                    .success(true)
                    .message("Successfully cancelled schedule")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Failed to cancel schedule: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
