package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.ManagerRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRequestLogRepository extends JpaRepository<ManagerRequestLog, Long> {
}
