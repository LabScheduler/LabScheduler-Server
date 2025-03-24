package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.ManagerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRequestRepository extends JpaRepository<ManagerRequest, Long> {
}
