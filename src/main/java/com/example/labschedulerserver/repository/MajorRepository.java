package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    public Major findMajorByName(String name);
}
