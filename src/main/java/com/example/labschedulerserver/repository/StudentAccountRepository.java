package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Long> {
    @Query(value = """
            
                SELECT sa.*
                FROM student_account sa
                JOIN account a ON sa.account_id = a.id
                WHERE a.role = 'STUDENT'
                AND (:classId IS NULL OR sa.class_id = :classId)
                AND (:majorId IS NULL OR sa.major_id = :majorId)
                AND (:code IS NULL OR sa.code LIKE CONCAT('%', :code, '%'));
                                                                               
            """, nativeQuery = true)
    List<StudentAccount> filterStudents(
            @Param("classId") Long classId,
            @Param("majorId") Long majorId,
            @Param("code") String code
    );
}
