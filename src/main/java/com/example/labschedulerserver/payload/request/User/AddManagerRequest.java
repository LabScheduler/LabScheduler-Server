package com.example.labschedulerserver.payload.request.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddManagerRequest {
    @JsonProperty("full_name")
    private String fullName;
    private String code;
    private String phone;
    private Boolean gender;
}
