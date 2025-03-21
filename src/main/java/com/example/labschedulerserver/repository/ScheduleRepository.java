package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    public List<Schedule> findByCourseSectionCourseSemesterId(Long semesterId);
}
