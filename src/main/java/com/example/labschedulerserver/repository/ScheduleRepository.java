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
    
    @Query("SELECT s FROM Schedule s JOIN s.courseSection cs JOIN cs.course c WHERE c.semester.id = :semesterId")
    List<Schedule> findByCourseSectionCourseSemesterId(@Param("semesterId") Long semesterId);
}
