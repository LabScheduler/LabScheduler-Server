package com.example.labschedulerserver.payload.response.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LecturerResponse {
    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    private String email;
    private String code;
    private String phone;
    private boolean gender;
    private String department;
    private String role;
    private String status;
}
