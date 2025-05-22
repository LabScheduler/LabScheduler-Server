package com.example.labschedulerserver.payload.request;

import lombok.Data;

@Data
public class ProcessRequest {

    private Long requestId;

    private String body;

    private String status;
}
