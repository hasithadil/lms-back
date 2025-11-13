package org.university.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseResponseDTO {
    private Long courseId;
    private String name;
    private Integer maxStudent;
    private Integer enrolledCount;
    private Integer availableSlots;
    private String lecturerName;
    private List<SubjectResponseDTO> subjects;
}
