package com.example.labschedulerserver.payload.request;

import com.example.labschedulerserver.common.RequestStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProcessRequest {
    @JsonProperty("request_id")
    private Long requestId;
    @JsonProperty("manager_id")
    private Long managerId;
    private String status;
}
