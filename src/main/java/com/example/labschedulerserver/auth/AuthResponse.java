package com.example.labschedulerserver.auth;

import com.example.labschedulerserver.common.AccountStatus;
import com.example.labschedulerserver.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private Integer id;
    private String token;
    private String email;
    private Role role;
    private String status;
    private Object accountInfo;

}
