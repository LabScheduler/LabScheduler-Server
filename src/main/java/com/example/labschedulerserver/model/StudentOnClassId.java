package com.example.labschedulerserver.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class StudentOnClassId implements Serializable {
    @JoinColumn(name = "student_id")
    private Long studentId;
    @JoinColumn(name = "class_id")
    private Long classId;

}
