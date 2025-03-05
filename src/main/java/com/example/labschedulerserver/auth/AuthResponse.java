package com.example.labschedulerserver.auth;

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
    private String token;
    private Role role;
    private List<String> permissions;
    private String fullName;
}
