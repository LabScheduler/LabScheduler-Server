package com.example.labschedulerserver.model;

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
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "major_id")
    @JsonIgnore
    private Major major;

    @OneToMany(mappedBy = "clazz")
    @JsonIgnore
    private List<StudentAccount> students;

    @OneToMany(mappedBy = "clazz")
    @JsonIgnore
    private List<Course> courses;
}
