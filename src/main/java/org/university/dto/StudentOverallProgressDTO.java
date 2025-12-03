package org.university.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentOverallProgressDTO {
    private Long studentId;
    private String studentName;
    private Integer totalEnrolledCourses;
    private List<CourseProgressDTO> coursesProgress;
}
