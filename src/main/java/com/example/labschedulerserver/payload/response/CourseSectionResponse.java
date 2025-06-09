package com.example.labschedulerserver.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseSectionResponse {
    private Long id;
    private int sectionNumber;
    private int maxStudentsInSection;
}
