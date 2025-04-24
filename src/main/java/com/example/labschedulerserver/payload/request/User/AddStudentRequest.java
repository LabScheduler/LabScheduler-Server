package com.example.labschedulerserver.payload.request.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddStudentRequest {
    @JsonProperty("full_name")
    private String fullName;

    private String email;

    private String code;

    private String phone;

    private Boolean gender;

    @JsonProperty("class_id")
    private Long classId;
}
