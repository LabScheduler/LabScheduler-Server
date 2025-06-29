package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository  extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);

    Optional<Subject> findByCode(String code);
}
