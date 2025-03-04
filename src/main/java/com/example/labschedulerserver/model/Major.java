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
@Table(name = "major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private String name;

    @OneToMany(mappedBy = "major")
    private List<Class> classes;

    @OneToMany(mappedBy = "major")
    private List<StudentAccount> studentAccounts;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


}
