package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "student_account")
public class StudentAccount {
    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    private boolean gender;

    private LocalDate birthday;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "students")
    private List<StudentOnClass> studentOnClasses;
}
