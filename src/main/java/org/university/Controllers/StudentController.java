package org.university.Controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.university.Repositories.EnrollmentRepo;
import org.university.Services.CourseService;
import org.university.Services.EnrollmentService;
import org.university.Services.StudentService;
import org.university.dto.CourseDTO;
import org.university.dto.CourseResponseDTO;
import org.university.dto.EnrollmentDTO;
import org.university.dto.StudentResponseDTO;

import java.util.List;

@Path("/student")
public class StudentController {
    @Inject
    EnrollmentService enrollmentService;

    @Inject
    StudentService studentService;

    @Inject
    CourseService courseService;

    @GET
    @Path("/{id}")
    public StudentResponseDTO getStudent(@PathParam("id") Long id){
        return studentService.getSpecficStudentDetails(id);
    }

    @POST
    @Path("/enroll")
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

    @GET
    @Path("/courses")
    public List<CourseDTO> getAllCourses(){
        return courseService.getAllCourses();
    }

    @GET
    @Path("/course/{id}")
    public CourseResponseDTO getCourse(@PathParam("id") Long id){
        return courseService.getCourseDetails(id);
    }

}
