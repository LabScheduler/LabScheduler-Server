package com.example.labschedulerserver.payload.response.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ManagerResponse {
    private Long id;
    private String fullName;
    private String email;
    private String code;
    private String phone;
    private boolean gender;
    private LocalDate birthday;
    private String role;
    private String status;
}
