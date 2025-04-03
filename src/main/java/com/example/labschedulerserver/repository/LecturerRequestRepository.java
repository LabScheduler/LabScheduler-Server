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
    SELECT * FROM LecturerRequest lr
    JOIN LecturerRequestLog lrl ON lr.id = lrl.lecturer_request_id
    WHERE lrl.status = 'PENDING'
    AND lrl.updated_at = (SELECT MAX(updated_at) FROM LecturerRequestLog WHERE lecturer_request_id = lr.id)
    """, nativeQuery = true)
    List<LecturerRequest> getAllPendingRequests();

    List<LecturerRequest> findAllByLecturerAccount(LecturerAccount lecturerAccount);
}
