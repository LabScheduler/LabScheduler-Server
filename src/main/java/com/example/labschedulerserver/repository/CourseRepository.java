package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
//    @Query(value = "SELECT EXISTS (SELECT 1 FROM Course WHERE subject_id = ?1 AND class_id = ?2 AND semester_id = ?3)", nativeQuery = true)
//    public boolean checkCourseExist(Integer subjectId, Integer classId, Integer semesterId);

    public Course findCoursesBySubjectIdAndClazzIdAndSemesterId(Integer subjectId, Integer classId, Integer semesterId);

    public List<Course> findAllBySemesterId(Integer semesterId);
}
