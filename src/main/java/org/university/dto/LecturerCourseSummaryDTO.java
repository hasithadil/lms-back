package org.university.dto;

import lombok.Data;

@Data
public class LecturerCourseSummaryDTO {
    private Long courseId;
    private String name;
    private Integer maxStudent;
}
