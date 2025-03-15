package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Clazz, Integer> {
    Optional<Clazz> findByName(String name);
}
