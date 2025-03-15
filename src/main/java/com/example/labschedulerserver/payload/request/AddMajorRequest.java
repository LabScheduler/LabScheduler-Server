package com.example.labschedulerserver.payload.request;

import com.example.labschedulerserver.model.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMajorRequest {
    private String name;
    private String code;
    private Integer departmentId;
}
