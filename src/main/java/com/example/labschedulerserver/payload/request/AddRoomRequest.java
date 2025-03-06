package com.example.labschedulerserver.payload.request;

import lombok.Data;

@Data
public class AddRoomRequest {
    private String name;
    private String location;
    private Integer capacity;
    private String status;
    private String description;
}
