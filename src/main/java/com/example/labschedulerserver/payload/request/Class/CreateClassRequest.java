package com.example.labschedulerserver.payload.request.Class;


import lombok.Data;

@Data

public class CreateClassRequest {
    private String name;
    private Long majorId;
    private String classType;

    private Long specializationId;
}
