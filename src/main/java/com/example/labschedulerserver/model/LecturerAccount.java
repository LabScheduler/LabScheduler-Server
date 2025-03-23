package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("account_id")
    private Long accountId;

    @Column(name = "full_name")
    @JsonProperty("full_name")
    private String fullName;

    private String code;

    private String phone;

    private boolean gender;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "lecturerAccount",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "lecturerAccount",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ScheduleRequest> scheduleRequests;

}
