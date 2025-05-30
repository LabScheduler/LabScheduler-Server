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
@Table(name = "lecturer_account")
public class LecturerAccount {
    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "full_name")
    private String fullName;

    private String code;

    private String phone;

    @Column(unique = true)
    private String email;

    private boolean gender;

    private LocalDate birthday;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(mappedBy = "lecturers")
    private List<Course> courses;



    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY)
    private List<Schedule> schedules;

}
