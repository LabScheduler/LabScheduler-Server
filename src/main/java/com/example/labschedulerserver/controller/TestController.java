package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.repository.ScheduleRepository;
import com.example.labschedulerserver.service.ScheduleService;
import com.example.labschedulerserver.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final ScheduleRepository scheduleRepository;
    private final SemesterService semesterService;
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<?> test(){
        Semester semester = semesterService.getCurrentSemester();
        System.out.println(semester.getName());
        return ResponseEntity.ok(scheduleService.allocateSchedule(8L));
    }
}
