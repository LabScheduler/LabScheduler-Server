package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "student_account")
public class StudentAccount {
    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String phone;

    private boolean gender;

    @OneToOne
    @MapsId
    private Account account;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;
}
