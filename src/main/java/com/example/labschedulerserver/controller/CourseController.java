package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.Course.CreateCourseRequest;
import com.example.labschedulerserver.payload.request.Course.UpdateCourseRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getAllCourse() {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCourse())
                .message("Get all courses in current semester successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "semesterId")
    public ResponseEntity<?> getAllCourseBySemester(@RequestParam Long semesterId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCourseBySemester(semesterId))
                .message("Get all courses by semester successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "classId")
    public ResponseEntity<?> getAllCourse(Long classId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCourse(classId))
                .message("Get all courses by class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = {"subjectId", "semesterId"})
    public ResponseEntity<?> getAllCourse(@RequestParam Long subjectId, @RequestParam Long semesterId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCourse(subjectId, semesterId))
                .message("Get all courses by subject and semester successfully")
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

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.createCourse(request))
                .message("Create course successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Delete course successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody UpdateCourseRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.updateCourse(id, request))
                .message("Update course successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/sections")
    public ResponseEntity<?> getCourseSectionByCourseId(@PathVariable Long courseId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getCourseSectionByCourseId(courseId))
                .message("Get course section by course id successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
