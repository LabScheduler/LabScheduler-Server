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

    private String code;

    private String phone;

    private boolean gender;

    @OneToOne
    @MapsId
    private Account account;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "lecturerAccount")
    private List<Course> courses;

    @OneToMany(mappedBy = "lecturerAccount")
    private List<ScheduleRequest> scheduleRequests;

}
