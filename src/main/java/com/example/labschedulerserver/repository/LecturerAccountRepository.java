package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.LecturerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerAccountRepository extends JpaRepository<LecturerAccount, Integer> {
}
