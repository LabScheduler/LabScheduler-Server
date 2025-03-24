package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RoomStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

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
    @JsonProperty("last_updated")
    private Timestamp lastUpdated;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "newRoom", fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonProperty("schedule_requests")
    private List<ManagerRequest> managerRequests;
}
