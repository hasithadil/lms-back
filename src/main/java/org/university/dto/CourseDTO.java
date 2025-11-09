package org.university.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private long courseId;
    private String name;
    private Integer maxStudent;
    private long lecturerId;
}
