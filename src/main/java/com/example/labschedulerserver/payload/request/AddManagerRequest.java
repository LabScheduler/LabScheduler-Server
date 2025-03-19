package com.example.labschedulerserver.payload.request;

import lombok.Data;

@Data
public class AddManagerRequest {
    private String email;
    private String password;
    private String fullName;
    private String code;
    private String phone;
    private Boolean gender;
}
