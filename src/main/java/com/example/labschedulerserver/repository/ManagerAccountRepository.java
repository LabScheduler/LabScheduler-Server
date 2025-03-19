package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.ManagerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerAccountRepository extends JpaRepository<ManagerAccount, Long> {
    public ManagerAccount findByAccount(Account account);
}
