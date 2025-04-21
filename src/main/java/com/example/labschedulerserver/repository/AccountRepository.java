package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = """
    SELECT email
    FROM (
             SELECT email, account_id FROM student_account
             UNION
             SELECT email, account_id FROM lecturer_account
             UNION
             SELECT email, account_id FROM manager_account
         ) AS unified_accounts
    WHERE account_id = :accountId
""", nativeQuery = true)
    Optional<String> findEmailByAccountId(@Param("accountId") Long accountId);
}
