package com.example.labschedulerserver.payload.request;

import com.example.labschedulerserver.model.Major;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class AddClassRequest {
    private String name;
    @JsonProperty("major_id")
    private Long majorId;

}
