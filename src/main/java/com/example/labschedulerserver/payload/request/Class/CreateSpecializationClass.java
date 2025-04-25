package com.example.labschedulerserver.payload.request.Class;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSpecializationClass {
    private String name;
    @JsonProperty("major_id")
    private Long majorId;
    @JsonProperty("specialization_id")
    private Long specializationId;
}
