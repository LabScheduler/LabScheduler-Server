package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.CourseSection;
import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.payload.request.AddCourseRequest;
import com.example.labschedulerserver.payload.response.CourseInfoResponse;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final LecturerAccountRepository lecturerAccountRepository;
    private final CourseSectionRepository courseSectionRepository;


    @Override
    public List<Course> getAllCoursesInSemester(Long semesterId) {
        return courseRepository.findAllBySemesterId(semesterId);
    }

    @Override
    public CourseInfoResponse getCourseById(Long id) {
            return CourseInfoResponse.builder()
                .course(courseRepository.findById(id).orElseThrow(()->new RuntimeException("Course not found")))
                .courseSections(courseSectionRepository.findAllByCourseId(id))
                .build();

    }

    @Override
    @Transactional
    public Course addNewCourse(AddCourseRequest request, Integer totalGroup) {
        Semester currentSemester = semesterRepository.findCurrentSemester(LocalDateTime.now()).get();
        Course course = courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(request.getSubjectId(), request.getClassId(), currentSemester.getId());
        if(course != null){
            throw new RuntimeException("Course already exist");
        }

        Course newCourse = Course.builder()
                .subject(subjectRepository.findById(request.getSubjectId()).get())
                .clazz(classRepository.findById(request.getClassId()).get())
                .lecturerAccount(lecturerAccountRepository.findById(request.getLecturerId()).get())
                .totalStudents(request.getTotalStudents())
                .semester(currentSemester)
                .build();
        courseRepository.save(newCourse);
        if(newCourse.getSubject().getTotalPracticePeriods() ==0){
            return newCourse;
        }
        List<CourseSection> courseSections = new ArrayList<>();
        CourseSection section = CourseSection.builder()
                .course(newCourse)
                .sectionNumber(0)
                .totalStudentsInSection(newCourse.getTotalStudents())
                .build();
        courseSections.add(section);
        for(int i =1; i<= totalGroup; i++){
            int totalStudent = newCourse.getTotalStudents();
            int totalStudentInSection = totalStudent/totalGroup;
            int remainingStudents = totalStudent % totalGroup;

            int studentsInThisSection = totalStudentInSection + (i <= remainingStudents ? 1 : 0);

            CourseSection newCourseSection = CourseSection.builder()
                    .course(newCourse)
                    .sectionNumber(i)
                    .totalStudentsInSection(studentsInThisSection)
                    .build();
            courseSections.add(newCourseSection);
        }
        courseSectionRepository.saveAll(courseSections);
        return newCourse;
    }

    @Override
    public void deleteCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(()->new RuntimeException("Course not found"));
        courseRepository.deleteById(id);
    }

    @Override
    public Course checkCourseExist(Long subjectId, Long classId, Long semesterId) {
        return courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(subjectId, classId, semesterId);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(()->new RuntimeException("Course not found with id: " + id));
        courseRepository.delete(course);

    }
}
