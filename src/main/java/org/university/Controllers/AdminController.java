package org.university.Controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.university.Services.CourseService;
import org.university.Services.LecturerService;
import org.university.Services.StudentService;
import org.university.dto.*;

import java.util.List;

@Path("/admin")
public class AdminController {

    @Inject
    StudentService studentService;

    @Inject
    CourseService courseService;

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

    @PUT
    @Path("/updatestudent/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StudentDTO updateStudent(StudentDTO dto, @PathParam("id") long id){
        return studentService.updateStudent(id,dto);
    }

    @DELETE
    @Path("/deletestudent/{id}")
    public void deleteStudent(@PathParam("id") long id){
        studentService.deleteStudent(id);
    }

    @Inject
    LecturerService lecturerService;

    @GET
    @Path("/getalllecturers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LecturerDTO> getAllLecturers(){
        return lecturerService.getAllLectures();
    }

    @POST
    @Path("/addlecturer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LecturerDTO createLecturer(LecturerDTO dto){
        return lecturerService.createLecturer(dto);
    }

    @PUT
    @Path("/updatelecturer/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LecturerDTO updateLecturer(@PathParam("id") long id, LecturerDTO dto){
        return lecturerService.updatelecturer(id,dto);
    }

    @DELETE
    @Path("/deletelecturer/{id}")
    public void deleteLecturer(@PathParam("id") long id){
        lecturerService.deleteLecturer(id);
    }

    @GET
    @Path("student/{id}")
    public StudentResponseDTO getStudent(@PathParam("id") Long id){
        return studentService.getSpecficStudentDetails(id);
    }

    @GET
    @Path("lecturer/{id}")
    public LecturerResponseDTO getLecturer(@PathParam("id") Long id){
        return lecturerService.getLecturerDetails(id);
    }

    @GET
    @Path("/course/{id}")
    public CourseResponseDTO getCourse(@PathParam("id") Long id){
        return courseService.getCourseDetails(id);
    }

}
