package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.SemesterWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterWeekRepository extends JpaRepository<SemesterWeek, Integer> {
}
