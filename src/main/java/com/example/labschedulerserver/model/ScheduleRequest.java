package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.common.RequestType;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleRequest {
    private UUID id;
    private Integer newDayOfWeek;
    private Integer newStartPeriod;
    private Integer newTotalPeriod;
    private String reason;
    private RequestStatus requestStatus;
    private RequestType requestType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
