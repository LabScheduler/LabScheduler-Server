package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.model.StudentOnClass;
import com.example.labschedulerserver.model.StudentOnClassId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentOnClassRepository extends JpaRepository<StudentOnClass, StudentOnClassId> {
}
