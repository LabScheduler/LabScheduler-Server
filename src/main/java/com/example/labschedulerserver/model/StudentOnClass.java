package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.StudentOnClassStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "student_on_class")
@Entity
public class StudentOnClass {
    @EmbeddedId
    private StudentOnClassId id;

    @Enumerated(EnumType.STRING)
    private StudentOnClassStatus status;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private StudentAccount students;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private Clazz clazz;
}
