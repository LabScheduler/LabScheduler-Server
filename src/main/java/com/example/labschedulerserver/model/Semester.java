package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "semester")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonProperty("end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "semester",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SemesterWeek> semesterWeeks;

    @OneToMany(mappedBy = "semester", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Course> courses;
}
