package com.example.labschedulerserver.payload.request.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddManagerRequest {

    private String fullName;
    private String code;
    private String email;
    private String phone;
    private Boolean gender;
}
