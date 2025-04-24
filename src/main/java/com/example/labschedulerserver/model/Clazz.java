package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.ClassType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="class")
public class Clazz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @Column(name = "class_type")
    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @ManyToMany(mappedBy = "classes")
    private List<StudentAccount> students;

    @OneToMany(mappedBy = "clazz")
    @JsonIgnore
    private List<Course> courses;
}
