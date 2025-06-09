package com.example.labschedulerserver.payload.request.Course;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.CourseSection;
import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.payload.response.CourseResponse;
import com.example.labschedulerserver.payload.response.CourseSectionResponse;
import com.example.labschedulerserver.repository.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseMapper {
    private final CourseRepository courseRepository;

    public CourseMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponse toCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .subject(course.getSubject().getName())
                .clazz(course.getClazz().getName())
                .semester(course.getSemester().getName())
                .lecturers(course.getLecturers().stream().map(LecturerAccount::getFullName).toList())
                .groupNumber(course.getGroupNumber())
                .maxStudents(course.getMaxStudents())
                .totalStudents(courseRepository.countCurrentStudentInCourse(course.getId()))
                .build();
    }

    public CourseSectionResponse toCourseSectionResponse(CourseSection courseSection) {
        int maxStudents = courseRepository.countCurrentStudentInCourseSection(courseSection.getId());
        if(maxStudents == 0 ) {
            maxStudents = courseSection.getMaxStudentsInSection();
        }
        return CourseSectionResponse.builder()
                .id(courseSection.getId())
                .sectionNumber(courseSection.getSectionNumber())
                .maxStudentsInSection(maxStudents)
                .build();
    }
}
