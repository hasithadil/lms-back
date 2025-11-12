package org.university.dto;

import lombok.Data;
import org.university.Models.Status;

import java.util.List;

@Data
public class LecturerResponseDTO {
    private Long lec_id;
    private String name;
    private String email;
    private Status status;
    private List<LecturerCourseSummaryDTO> courses;
    private List<LecturerSubjectDTO> subjects;
}
