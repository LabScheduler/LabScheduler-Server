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
@Table(name = "major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @OneToMany(mappedBy = "major", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Clazz> classes;

    @OneToMany(mappedBy = "major", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StudentAccount> studentAccounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;


}
