package org.university.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseProgressDTO {
    private Long courseId;
    private String courseName;
    private Integer totalSubjects;
    private Integer totalTopics;
    private Integer completedTopics;
    private Double overallCompletionPercentage;
    private List<SubjectProgressDTO> subjectsProgress;
}
