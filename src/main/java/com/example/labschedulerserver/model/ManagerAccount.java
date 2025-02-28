package com.example.labschedulerserver.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerAccount {
    private UUID accountId;
    private String fullName;
    private String code;
    private String phone;
    private boolean gender;
}
