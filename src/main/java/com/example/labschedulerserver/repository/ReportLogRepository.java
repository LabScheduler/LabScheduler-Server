package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {
}
