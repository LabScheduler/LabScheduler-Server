package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.model.LecturerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRequestRepository extends JpaRepository<LecturerRequest, Long> {
    @Query("SELECT r FROM LecturerRequest r WHERE r.lecturerAccount.accountId = :lecturerId")
    List<LecturerRequest> findByLecturerAccountId(@Param("lecturerId") Long lecturerId);

    @Query(value = """
            SELECT lr.*, lrl.status\s
            FROM lecturer_request lr
            JOIN lecturer_request_log lrl ON lr.id = lrl.request_id
            WHERE lrl.status = 'PENDING';
            """, nativeQuery = true)
    List<LecturerRequest> getAllPendingRequests();

    List<LecturerRequest> findAllByLecturerAccount(LecturerAccount lecturerAccount);
}
