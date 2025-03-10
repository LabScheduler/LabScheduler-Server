package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.LecturerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecturerAccountRepository extends JpaRepository<LecturerAccount, Integer> {
    public Object findByAccount(Account account);
}
