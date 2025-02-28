package com.example.labschedulerserver.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {
    private UUID id;
    private String name;
}
