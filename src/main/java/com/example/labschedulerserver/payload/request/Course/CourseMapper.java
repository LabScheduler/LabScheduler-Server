package com.example.labschedulerserver.payload.request.Course;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.payload.response.CourseResponse;

public class CourseMapper {
    public static CourseResponse toCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .subject(course.getSubject().getName())
                .clazz(course.getClazz().getName())
                .semester(course.getSemester().getName())
                .lecturer(course.getLecturerAccount().getFullName())
                .groupNumber(course.getGroupNumber())
                .totalStudents(course.getTotalStudents())
                .totalSection(course.getCourseSections().size())
                .build();
    }
}
