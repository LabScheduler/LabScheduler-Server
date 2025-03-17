package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Course;
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
    private final SemesterService semesterService;

    @GetMapping
    public ResponseEntity<?> getAllCourseInSemester(@RequestParam("semester_id") Integer semesterId) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getAllCoursesInSemester(semesterId).stream())
                .message("Get all courses in semester successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Integer id) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(courseService.getCourseById(id))
                .message("Get course by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }








//    @GetMapping("/check")
//    public ResponseEntity<?> checkCourse(@RequestParam("subject_id") Integer subjectId, @RequestParam("class_id") Integer classId, @RequestParam("semester_id") Integer semesterId) {
//        DataResponse response = DataResponse.builder()
//                .success(true)
//                .data(courseService.checkCourseExist(subjectId, classId, semesterId))
//                .build();
//        return ResponseEntity.ok(response);
//    }
}
