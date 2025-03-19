package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
}
