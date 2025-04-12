package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.CourseSection;
import com.example.labschedulerserver.payload.request.Course.CreateCourseRequest;
import com.example.labschedulerserver.payload.request.Course.UpdateCourseRequest;
import com.example.labschedulerserver.payload.response.CourseResponse;

import java.util.List;

public interface CourseService {
    public List<CourseResponse> getAllCourse();
    public List<CourseResponse> getAllCourseBySemester(Long semesterId);
    public List<CourseResponse> getAllCourse(Long classId);
    public List<CourseResponse> getAllCourse(Long subjectId, Long semesterId);

    public CourseResponse getCourseById(Long id);

    public List<CourseSection> getCourseSectionByCourseId(Long courseId);

    public CourseResponse createCourse(CreateCourseRequest request);
    public void deleteCourse(Long id);
    public Course checkCourseExist(Long subjectId, Long classId, Long semesterId);

    public CourseResponse updateCourse(Long id, UpdateCourseRequest request);
}
