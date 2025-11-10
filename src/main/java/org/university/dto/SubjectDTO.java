package org.university.dto;

import lombok.Data;

@Data
public class SubjectDTO {
    private Long subId;      // for returning data (null when creating)
    private String subjectName;

    private Long lecturerId;     // ✅ we use lecturerId (not LecturerModel)
}
