package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.CourseSection;
import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.payload.request.Course.CourseMapper;
import com.example.labschedulerserver.payload.request.Course.CreateCourseRequest;
import com.example.labschedulerserver.payload.request.Course.UpdateCourseRequest;
import com.example.labschedulerserver.payload.response.CourseResponse;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final LecturerAccountRepository lecturerAccountRepository;
    private final CourseSectionRepository courseSectionRepository;

    private final ModelMapper modelMapper;

    @Override //Get all courses in current semester
    public List<CourseResponse> getAllCourse(){
        Semester semester = semesterRepository.findCurrentSemester().orElseThrow(()->new ResourceNotFoundException("Semester not found"));
        return courseRepository
                .findAllBySemesterId(semester.getId())
                .stream()
                .map(CourseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getAllCourseBySemester(Long semesterId) {
        return courseRepository.findAllBySemesterId(semesterId)
                .stream()
                .map(CourseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getAllCourse(Long classId) {
        return courseRepository
                .findAllByClazzId(classId)
                .stream()
                .map(CourseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getAllCourse(Long subjectId, Long semesterId) {
        return courseRepository.findAllBySubjectIdAndSemesterId(subjectId,semesterId)
                .stream()
                .map(CourseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::toCourseResponse)
                .orElseThrow(()->new ResourceNotFoundException("Course not found with id:" +id));
    }

    @Override
    public List<CourseSection> getCourseSectionByCourseId(Long courseId) {
        List<CourseSection> courseSections = courseSectionRepository.findAllByCourseId(courseId);
        if(courseSections.isEmpty()){
            throw new ResourceNotFoundException("CourseSection not found with courseId: " + courseId);
        }
        return courseSections;
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        Semester semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(()->new ResourceNotFoundException("Semester not found"));
        Course course = courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(request.getSubjectId(), request.getClassId(), semester.getId());
        if(course != null){
            throw new ResourceNotFoundException("Course already exist");
        }

        Course newCourse = Course.builder()
                .subject(subjectRepository.findById(request.getSubjectId()).orElseThrow(()->new ResourceNotFoundException("Subject not found")))
                .clazz(classRepository.findById(request.getClassId()).orElseThrow(()->new ResourceNotFoundException("Class not found")))
                .lecturerAccount(lecturerAccountRepository.findById(request.getLecturerId()).orElseThrow(()->new ResourceNotFoundException("Lecturer not found")))
                .groupNumber(courseRepository
                        .findAllBySubjectIdAndSemesterId(request.getSubjectId(), semester.getId())
                        .stream()
                        .mapToInt(Course::getGroupNumber)
                        .max()
                        .orElse(0)+1
                )
                .totalStudents(request.getTotalStudents())
                .semester(semester)
                .build();
        if(newCourse.getSubject().getTotalPracticePeriods() == 0){
            return CourseMapper.toCourseResponse(courseRepository.save(newCourse));
        }
        List<CourseSection> courseSections = new ArrayList<>();
        for(int i =1; i<= request.getTotalSection(); i++){
            int totalStudent = newCourse.getTotalStudents();
            int totalStudentInSection = totalStudent/ request.getTotalSection();
            int remainingStudents = totalStudent % request.getTotalSection();

            int studentsInThisSection = totalStudentInSection + (i <= remainingStudents ? 1 : 0);

            CourseSection newCourseSection = CourseSection.builder()
                    .course(newCourse)
                    .sectionNumber(i)
                    .totalStudentsInSection(studentsInThisSection)
                    .build();
            courseSections.add(newCourseSection);
        }
        newCourse.setCourseSections(courseSections);
        System.out.println(newCourse.getCourseSections().size());
        courseRepository.save(newCourse);
        courseSectionRepository.saveAll(courseSections);
        return CourseMapper.toCourseResponse(newCourse);
    }


    @Override
    public Course checkCourseExist(Long subjectId, Long classId, Long semesterId) {
        return courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(subjectId, classId, semesterId);
    }

    @Override
    public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Course not found with id: " + id));

        course.setSubject(subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> new ResourceNotFoundException("Subject not found")));
        course.setClazz(classRepository.findById(request.getClassId()).orElseThrow(() -> new ResourceNotFoundException("Class not found")));
        course.setLecturerAccount(lecturerAccountRepository.findById(request.getLecturerId()).orElseThrow(() -> new ResourceNotFoundException("Lecturer not found")));
        course.setTotalStudents(request.getTotalStudents());

        Course updatedCourse = courseRepository.save(course);
        return CourseMapper.toCourseResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Course not found with id: " + id));
        courseRepository.delete(course);
    }
}
