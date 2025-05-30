package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByAuthorId(Long authorId);
}
