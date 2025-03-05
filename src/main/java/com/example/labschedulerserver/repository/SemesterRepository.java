package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
}
