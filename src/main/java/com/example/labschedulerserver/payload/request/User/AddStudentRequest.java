package com.example.labschedulerserver.payload.request.User;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddStudentRequest {

    private String fullName;

    private String email;

    private String code;

    private String phone;

    private Boolean gender;

    private LocalDate birthday;

    private Long classId;
}
