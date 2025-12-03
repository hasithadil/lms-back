package org.university.dto;

import lombok.Data;

@Data
public class TopicDTO {
    private Long topicId;
    private String topicName;
    private Integer orderIndex;
    private Long subjectId;
}
