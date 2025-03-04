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
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Major> majors;

    @OneToMany(mappedBy = "department")
    private List<LecturerAccount> lecturerAccounts;
}
