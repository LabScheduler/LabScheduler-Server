package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.payload.request.AddCourseRequest;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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



    @Override
    public List<Course> getAllCoursesInSemester(Integer semesterId) {
        return courseRepository.findAllBySemesterId(semesterId);
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseRepository.findById(id).orElseThrow(()->new RuntimeException("Course not found"));
    }

    @Override
    public Course addNewCourse(AddCourseRequest request) {
        Semester currentSemester = semesterRepository.findCurrentSemester(LocalDateTime.now()).get();
        Course course = courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(request.getSubjectId(), request.getClassId(), currentSemester.getId());
        if(course != null){
            throw new RuntimeException("Course already exist");
        }


        return Course.builder()
                .semester(currentSemester)
                .subject(subjectRepository.findById(request.getSubjectId()).get())
                .clazz(classRepository.findById(request.getClassId()).get())
                .build();
    }

    @Override
    public void deleteCourseById(Integer id) {

    }

    @Override
    public Course updateCourse(Integer id, Map<String, Object> payload) {
        return null;
    }

    @Override
    public Course checkCourseExist(Integer subjectId, Integer classId, Integer semesterId) {
        return courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(subjectId, classId, semesterId);
    }
}
