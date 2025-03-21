package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddLecturerRequest {
    private String email;
    private String password;
    @JsonProperty("full_name")
    private String fullName;
    private String code;
    private String phone;
    private Boolean gender;
    @JsonProperty("department_id")
    private String departmentId;
}
