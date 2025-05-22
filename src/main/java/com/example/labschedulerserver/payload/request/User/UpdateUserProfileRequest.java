package com.example.labschedulerserver.payload.request.User;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserProfileRequest {
    private String email;
    private String phone;
}
