package com.example.labschedulerserver.payload.request;

import lombok.Data;

@Data

public class AddRoomRequest {
    private String name;
    private Integer capacity;
    private String description;
}
