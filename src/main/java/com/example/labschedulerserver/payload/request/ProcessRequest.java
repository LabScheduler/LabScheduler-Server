package com.example.labschedulerserver.payload.request;

import com.example.labschedulerserver.common.RequestStatus;
import lombok.Data;

@Data
public class ProcessRequest {
    private Long requestId;
    private Long managerId;
    private String status;
}
