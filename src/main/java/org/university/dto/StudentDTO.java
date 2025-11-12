package org.university.dto;

import lombok.Data;
import org.university.Models.Status;

@Data
public class StudentDTO {

    private Long s_id;
    private String kc_id;
    private String email;
    private String firstName;
    private String lastName;
    private Status status;
}
