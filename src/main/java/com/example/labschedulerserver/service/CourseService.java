package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.payload.request.AddCourseRequest;
import com.example.labschedulerserver.payload.response.CourseInfoResponse;

import java.util.List;
import java.util.Map;

public interface CourseService {
    public List<Course> getAllCoursesInSemester(Long semesterId);
    public CourseInfoResponse getCourseById(Long id);
    public Course addNewCourse(AddCourseRequest request, Integer totalGroup);
    public void deleteCourseById(Long id);
//    public Course updateCourse(Integer id, Map<String, Object> payload);
    public Course checkCourseExist(Long subjectId, Long classId, Long semesterId);
    public void deleteCourse(Long id);
}
