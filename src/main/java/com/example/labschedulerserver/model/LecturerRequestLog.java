package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lecturer_request_log")
public class LecturerRequestLog {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "request_id")
    private LecturerRequest request;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private ManagerAccount managerAccount;

    private String body;

    @Column(name = "replied_at")
    private Timestamp repliedAt;
}
