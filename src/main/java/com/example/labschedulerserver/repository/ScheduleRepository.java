package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query(value = "SELECT s.* FROM schedule s " +
            "JOIN semester_week sw ON s.semester_week_id = sw.id " +
            "JOIN semester sem ON sw.semester_id = sem.id " +
            "WHERE CURDATE() BETWEEN sem.start_date AND sem.end_date",
            nativeQuery = true)
    List<Schedule> findAllByCurrentSemester();
    
    @Query(value = "SELECT s.* FROM schedule s " +
            "JOIN semester_week sw ON s.semester_week_id = sw.id " +
            "WHERE sw.semester_id = :semesterId",
            nativeQuery = true)
    List<Schedule> findAllBySemesterId(@Param("semesterId") Long semesterId);
    
    @Query(value = "SELECT s.* FROM schedule s " +
            "JOIN course_section cs ON s.course_section_id = cs.id " +
            "JOIN course c ON cs.course_id = c.id " +
            "WHERE c.class_id = :classId " +
            "AND c.semester_id = (SELECT id FROM semester WHERE CURDATE() BETWEEN start_date AND end_date LIMIT 1)",
            nativeQuery = true)
    List<Schedule> findAllByClassIdInCurrentSemester(@Param("classId") Long classId);
    
    @Query(value = "SELECT s.* FROM schedule s " +
            "JOIN course_section cs ON s.course_section_id = cs.id " +
            "WHERE cs.course_id = :courseId",
            nativeQuery = true)
    List<Schedule> findAllByCourseId(@Param("courseId") Long courseId);
    
    @Query(value = "SELECT s.* FROM schedule s " +
            "JOIN course_section cs ON s.course_section_id = cs.id " +
            "JOIN course c ON cs.course_id = c.id " +
            "WHERE c.lecturer_id = :lecturerId " +
            "AND c.semester_id = (SELECT id FROM semester WHERE CURDATE() BETWEEN start_date AND end_date LIMIT 1)",
            nativeQuery = true)
    List<Schedule> findAllByLecturerIdInCurrentSemester(@Param("lecturerId") Long lecturerId);
    
    @Query(value = "SELECT s.* FROM schedule s " +
            "WHERE s.semester_week_id = :weekId",
            nativeQuery = true)
    List<Schedule> findAllByWeekId(@Param("weekId") Long weekId);
}
