package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.ScheduleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRequestRepository extends JpaRepository<ScheduleRequest, Integer> {
}
