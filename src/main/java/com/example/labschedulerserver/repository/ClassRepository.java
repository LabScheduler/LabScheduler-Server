package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Clazz, Integer> {
}
