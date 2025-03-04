package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String location;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private String description;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastUpdated;

    @OneToMany(mappedBy = "room")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "newRoom")
    private List<ScheduleRequest> scheduleRequests;
}
