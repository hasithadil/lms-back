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


    @Inject
    LecturerService lecturerService;

    @GET
    @Path("/students")
    public List<StudentDTO> getAllStudents(){
        return studentService.getAllStudents();
    }

    @POST
    @Path("/students")
    public StudentDTO addStudent(StudentDTO dto){
        return studentService.createStudent(dto);
    }

    @PUT
    @Path("/student/{id}")
    public StudentDTO updateStudent(StudentDTO dto, @PathParam("id") Long id){
        return studentService.updateStudent(id,dto);
    }

    @DELETE
    @Path("/student/{id}")
    public void deleteStudent(@PathParam("id") long id){
        studentService.deleteStudent(id);
    }


    @GET
    @Path("/lecturers")
    public List<LecturerDTO> getAllLecturers(){
        return lecturerService.getAllLectures();
    }

    @POST
    @Path("/lecturers")
    public LecturerDTO createLecturer(LecturerDTO dto){
        return lecturerService.createLecturer(dto);
    }

    @PUT
    @Path("/lecturer/{id}")
    public LecturerDTO updateLecturer(@PathParam("id") long id, LecturerDTO dto){
        return lecturerService.updatelecturer(id,dto);
    }

    @DELETE
    @Path("/lecturer/{id}")
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
