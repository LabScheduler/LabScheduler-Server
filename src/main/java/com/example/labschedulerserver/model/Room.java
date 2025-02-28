package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RoomStatus;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    private UUID id;
    private String name;
    private String location;
    private Integer capacity;
    private RoomStatus status;
    private String description;
    private Timestamp lastUpdated;
}
