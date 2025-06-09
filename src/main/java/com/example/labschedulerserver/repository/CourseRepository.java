package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
//    @Query(value = "SELECT EXISTS (SELECT 1 FROM Course WHERE subject_id = ?1 AND class_id = ?2 AND semester_id = ?3)", nativeQuery = true)
//    public boolean checkCourseExist(Integer subjectId, Integer classId, Integer semesterId);

    //Check if exist
    public Course findCoursesBySubjectIdAndClazzIdAndSemesterId(Long subjectId, Long classId, Long semesterId);



    public List<Course> findAllBySemesterId(Long semesterId);

    public List<Course> findAllByClazzId(Long classId);

    public List<Course> findAllBySubjectIdAndSemesterId(Long subjectId, Long semesterId);

    @Query(value = """
        SELECT COUNT(*)
        FROM student_on_course_section socs
        JOIN course_section cs ON socs.course_section_id = cs.id
        WHERE cs.course_id = :courseId
        """, nativeQuery = true)
    int countCurrentStudentInCourse(Long courseId);

    @Query(value = """
        SELECT COUNT(*)
        FROM student_on_course_section socs
        JOIN course_section cs ON socs.course_section_id = cs.id
        WHERE cs.id = :courseSectionId;
        """, nativeQuery = true)
    int countCurrentStudentInCourseSection(Long courseSectionId);
}
