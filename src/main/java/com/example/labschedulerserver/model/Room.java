package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.common.RoomType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Timestamp lastUpdated;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Schedule> schedules;

    @Enumerated(EnumType.STRING)
    private RoomType type;
}
