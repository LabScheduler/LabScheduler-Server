package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RequestStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lecturer_request_log")
public class    LecturerRequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id")
    private LecturerRequest request;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonProperty("manager_account")
    private ManagerAccount managerAccount;

    private String body;

    @Column(name = "replied_at")
    @JsonProperty("replied_at")
    private Timestamp repliedAt;
}
