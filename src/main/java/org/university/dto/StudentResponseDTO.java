package org.university.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String status;
    private List<CourseSummaryDTO> enrollments; // Courses student enrolled
}
