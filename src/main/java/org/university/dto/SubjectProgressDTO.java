package org.university.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubjectProgressDTO {
    private Long subjectId;
    private String subjectName;
    private Integer totalTopics;
    private Integer completedTopics;
    private Double completionPercentage;
    private List<TopicProgressDTO> topics;
}
