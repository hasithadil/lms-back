package org.university.Controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.university.Services.StudentService;
import org.university.dto.StudentDTO;

import java.util.List;

@Path("/admin")
public class AdminController {

    @Inject
    StudentService studentService;

    @GET
    @Path("/allstudents")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StudentDTO> getAllStudents(){
        return studentService.getAllStudents();
    }

    @POST
    @Path("/addstudent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StudentDTO addStudent(StudentDTO dto){
        return studentService.createStudent(dto);
    }

}
