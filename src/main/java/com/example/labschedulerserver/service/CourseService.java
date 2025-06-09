package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.CourseSection;
import com.example.labschedulerserver.payload.request.Course.CreateCourseRequest;
import com.example.labschedulerserver.payload.request.Course.UpdateCourseRequest;
import com.example.labschedulerserver.payload.response.CourseResponse;
import com.example.labschedulerserver.payload.response.CourseSectionResponse;
import com.example.labschedulerserver.payload.response.NewCourseResponse;
import com.example.labschedulerserver.payload.response.User.LecturerResponse;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourse();

    List<CourseResponse> getAllCourseBySemester(Long semesterId);

    List<CourseResponse> getAllCourse(Long classId);

    List<CourseResponse> getAllCourse(Long subjectId, Long semesterId);

    CourseResponse getCourseById(Long id);

    List<CourseSectionResponse> getCourseSectionByCourseId(Long courseId);

    NewCourseResponse createCourse(CreateCourseRequest request);

    void deleteCourse(Long id);

    Course checkCourseExist(Long subjectId, Long classId, Long semesterId);

    List<CourseResponse> getLecturerCourse();

    CourseResponse updateCourse(Long id, UpdateCourseRequest request);

    List<LecturerResponse> getLecturersByCourse(Long courseId);
}
