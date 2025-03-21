package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RoomStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private String description;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Timestamp lastUpdated;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "newRoom", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ScheduleRequest> scheduleRequests;
}
