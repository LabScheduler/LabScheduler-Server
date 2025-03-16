package com.example.labschedulerserver.payload.request;

import com.example.labschedulerserver.model.Major;
import lombok.Data;

@Data

public class AddClassRequest {
    private String name;
    private Integer majorId;

}
