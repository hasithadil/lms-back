package org.university.dto;

import lombok.Data;
import org.university.Models.CourseModel;
import org.university.Models.StudentModel;

import java.time.LocalDate;

@Data
public class EnrollmentDTO {
    private long enrollmentId;
    private long studentId;
    private long courseId;
    private LocalDate enrollmentDate;
}
