package org.university;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.university.dto.ErrorResponseDTO;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable ex) {
        int status;
        String message = ex.getMessage();

        if (ex instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND.getStatusCode();
        } else if (ex instanceof IllegalStateException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            message = "Something went wrong, please try again later.";
        }

        ErrorResponseDTO error = new ErrorResponseDTO(
                status,
                message,
                uriInfo != null ? uriInfo.getPath() : "unknown"
        );

        return Response.status(status).entity(error).build();
    }
}