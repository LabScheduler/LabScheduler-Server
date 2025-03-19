package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    @Query("SELECT s FROM Semester s WHERE :now BETWEEN s.startDate AND s.endDate")
    Optional<Semester> findCurrentSemester(@Param("now") LocalDateTime now);
}
