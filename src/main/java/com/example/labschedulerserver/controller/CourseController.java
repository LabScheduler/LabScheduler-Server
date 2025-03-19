package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.payload.request.AddCourseRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.CourseService;
import com.example.labschedulerserver.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getAllCourseInSemester(@RequestParam("semester_id") Long semesterId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCoursesInSemester(semesterId).stream())
                .message("Get all courses in semester successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getCourseById(id))
                .message("Get course by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody AddCourseRequest request, @RequestParam("total_group") Integer totalGroup) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.addNewCourse(request,totalGroup))
                .message("Create course successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Delete course successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
