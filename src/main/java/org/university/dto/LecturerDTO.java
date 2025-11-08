package org.university.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LecturerDTO {
    private Long lec_id;

    private String kc_id;  // Keycloak user id (links login user)

    private String email;

    private String firstName;

    private String lastName;

    private String status; // ACTIVE / INACTIVE

}
