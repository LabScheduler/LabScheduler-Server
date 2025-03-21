package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddStudentRequest {
    private String email;
    private String password;
    @JsonProperty("full_name")
    private String fullName;
    private String code;
    private String phone;
    private Boolean gender;
    @JsonProperty("major_id")
    private String majorId;
    @JsonProperty("class_id")
    private String classId;
}
