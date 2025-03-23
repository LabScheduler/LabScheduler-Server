package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.SemesterWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterWeekRepository extends JpaRepository<SemesterWeek, Long> {
    public List<SemesterWeek> findBySemesterId(Long semesterId);
}
