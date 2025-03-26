package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.LecturerRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecturerRequestLogRepository extends JpaRepository<LecturerRequestLog, Long> {
    
    @Query("SELECT l FROM LecturerRequestLog l WHERE l.request.id = :requestId")
    Optional<LecturerRequestLog> findByRequestId(@Param("requestId") Long requestId);
}
