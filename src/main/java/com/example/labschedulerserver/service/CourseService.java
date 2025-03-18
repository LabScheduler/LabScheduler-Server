package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.payload.request.AddCourseRequest;

import java.util.List;
import java.util.Map;

public interface CourseService {
    public List<Course> getAllCoursesInSemester(Integer semesterId);
    public Course getCourseById(Integer id);
    public Course addNewCourse(AddCourseRequest request);
    public void deleteCourseById(Integer id);
//    public Course updateCourse(Integer id, Map<String, Object> payload);
    public Course checkCourseExist(Integer subjectId, Integer classId, Integer semesterId);

}
