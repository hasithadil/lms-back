package org.university.dto;

import lombok.Data;

@Data
public class TopicProgressDTO {
    private Long progressId;
    private Long topicId;
    private String topicName;
    private Long studentId;
    private Boolean isCompleted;
}
