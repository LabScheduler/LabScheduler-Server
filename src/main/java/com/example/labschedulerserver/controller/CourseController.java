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

    @GetMapping(params = "semester_id")
    public ResponseEntity<?> getAllCourseBySemester(@RequestParam("semester_id") Long semesterId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCourseBySemester(semesterId))
                .message("Get all courses by semester successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "class_id")
    public ResponseEntity<?> getAllCourse(@RequestParam("class_id") Long classId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCourse(classId))
                .message("Get all courses by class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = {"subject_id", "semester_id"})
    public ResponseEntity<?> getAllCourse(@RequestParam("subject_id") Long subjectId, @RequestParam("semester_id") Long semesterId) {
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

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.createCourse(request))
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

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody UpdateCourseRequest request) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.updateCourse(id, request))
                .message("Update course successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/section/{courseId}")
    public ResponseEntity<?> getCourseSectionByCourseId(@PathVariable Long courseId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getCourseSectionByCourseId(courseId))
                .message("Get course section by course id successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
