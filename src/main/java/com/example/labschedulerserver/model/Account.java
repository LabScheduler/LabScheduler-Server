package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.AccountStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private UUID id;
    private String email;
    private String password;
    private Role role;
    private AccountStatus status;
}
