package org.university.dto;

import lombok.Data;
import org.university.Models.CourseModel;
import org.university.Models.SubjectModel;

@Data
public class CourseSubjectDTO {
    private long courseId;
    private long subjectId;
}
