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
@Table(name = "lecturer_account")
public class LecturerAccount {
    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(unique = true, nullable = false)
    private String phone;

    private boolean gender;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "lecturerAccount", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "lecturerAccount", fetch = FetchType.LAZY)
    private List<ScheduleRequest> scheduleRequests;

}
