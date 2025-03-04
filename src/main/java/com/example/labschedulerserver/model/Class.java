package com.example.labschedulerserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @OneToMany(mappedBy = "clazz")
    private List<StudentAccount> students;

    @OneToMany(mappedBy = "clazz")
    private List<Course> courses;
}
