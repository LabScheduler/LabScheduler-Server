package com.example.labschedulerserver.payload.request.Class;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateClassRequest {
    private String name;
    private Long majorId;
    @Nullable
    private Long specializationId;
}
