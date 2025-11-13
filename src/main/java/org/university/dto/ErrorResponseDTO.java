package org.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private int status;        // HTTP status code
    private String message;    // readable message
    private String path;       // request path
}
