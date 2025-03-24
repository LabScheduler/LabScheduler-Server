package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "manager_account")
public class ManagerAccount {
    @Id
    @Column(name = "account_id")
    @JsonProperty("account_id")
    private Long accountId;

    @Column(name = "full_name")
    @JsonProperty("full_name")
    private String fullName;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String phone;

    private boolean gender;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "managerAccount", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ManagerRequestLog> managerRequestLogs;
}
