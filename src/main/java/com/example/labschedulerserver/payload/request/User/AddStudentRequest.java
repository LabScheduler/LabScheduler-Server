package com.example.labschedulerserver.payload.request.User;

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
    private Long majorId;
    @JsonProperty("class_id")
    private Long classId;
}
