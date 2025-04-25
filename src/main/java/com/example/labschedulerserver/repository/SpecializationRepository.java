package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
}
