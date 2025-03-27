package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.LecturerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRequestRepository extends JpaRepository<LecturerRequest, Long> {
    
    @Query("SELECT r FROM LecturerRequest r WHERE r.lecturerRequestLog IS NULL")
    List<LecturerRequest> findByLecturerRequestLogIsNull();
    
    @Query("SELECT r FROM LecturerRequest r WHERE r.lecturerAccount.accountId = :lecturerId")
    List<LecturerRequest> findByLecturerAccountId(@Param("lecturerId") Long lecturerId);
}
