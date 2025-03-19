package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Long> {
    public Object findByAccount(Account account);
}
