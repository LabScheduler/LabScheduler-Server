package com.example.labschedulerserver.payload.request.Class;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class AddClassRequest {
    private String name;
    @JsonProperty("major_id")
    private Long majorId;

}
