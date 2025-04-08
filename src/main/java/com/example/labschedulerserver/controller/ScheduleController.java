package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.UpdateScheduleRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;
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
        List<ScheduleResponse> schedules = scheduleService.allocateSchedule(courseId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully allocated schedules for course")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllSchedulesInCurrentSemester() {
        List<ScheduleResponse> schedules = scheduleService.getAllScheduleInSemester();
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for current semester")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<?> getSchedulesInSemester(@PathVariable Long semesterId) {
        List<ScheduleResponse> schedules = scheduleService.getAllScheduleBySemesterId(semesterId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for semester")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getSchedulesByClass(@PathVariable Long classId) {
        List<ScheduleResponse> schedules = scheduleService.getAllScheduleByClassId(classId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for class")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getSchedulesByCourse(@PathVariable Long courseId) {
        List<ScheduleResponse> schedules = scheduleService.getAllScheduleByCourseId(courseId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for course")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<?> getSchedulesByLecturer(@PathVariable Long lecturerId) {
        List<ScheduleResponse> schedules = scheduleService.getAllScheduleByLecturerId(lecturerId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for lecturer")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/week/{weekId}")
    public ResponseEntity<?> getSchedulesInWeek(@PathVariable Long weekId) {
        List<ScheduleResponse> schedules = scheduleService.getAllSchedulesInSpecificWeek(weekId);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully retrieved schedules for week")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest request) {
        ScheduleResponse schedules = scheduleService.createSchedule(request);
        DataResponse response = DataResponse.builder()
                .data(schedules)
                .success(true)
                .message("Successfully created schedule")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancel/{scheduleId}")
    public ResponseEntity<?> cancelSchedule(@PathVariable Long scheduleId) {
        ScheduleResponse schedule = scheduleService.cancelSchedule(scheduleId);
        DataResponse response = DataResponse.builder()
                .data(schedule)
                .success(true)
                .message("Successfully cancelled schedule")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateSchedule(@RequestBody UpdateScheduleRequest request) {
        DataResponse response = DataResponse.builder()
                .data(scheduleService.updateSchedule(request))
                .success(true)
                .message("Successfully updated schedule")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterSchedule(@RequestParam(name = "semester_id") Long semesterId,
                                            @RequestParam(name = "course_id", required = false) Long courseId,
                                            @RequestParam(name = "class_id", required = false) Long classId,
                                            @RequestParam(name = "lecturer_id", required = false) Long lecturerId) {
            DataResponse response = DataResponse.builder()
                    .data(scheduleService.filterSchedule(semesterId, classId, courseId, lecturerId))
                    .success(true)
                    .message("Successfully filtered schedules")
                    .build();
            return ResponseEntity.ok(response);
    }
}
