package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "class_id")
    @JsonProperty("class")
    private Clazz clazz;
}
