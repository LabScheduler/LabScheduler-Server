package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.ManagerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerAccountRepository extends JpaRepository<ManagerAccount, Integer> {
}
