package org.university.Controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.university.Repositories.EnrollmentRepo;
import org.university.Services.EnrollmentService;
import org.university.dto.EnrollmentDTO;

import java.util.List;

@Path("student")
public class StudentController {
    @Inject
    EnrollmentService enrollmentService;

    @POST
    @Path("/enrollment")
    public EnrollmentDTO enrollment(EnrollmentDTO dto){
        return enrollmentService.enrollStudent(dto);
    }

    @GET
    @Path("/enroll/{id}")
    public List<EnrollmentDTO> getEnrollments(@PathParam("id")  long id){
        return enrollmentService.getEnrollmentsByStudent(id);
    }

    @DELETE
    @Path("/unenroll/{studentId}/{courseId}")
    public void unenroll(@PathParam("studentId") long studentId, @PathParam("courseId") long courseId){
        enrollmentService.unenrollStudent(studentId, courseId);
    }

}
