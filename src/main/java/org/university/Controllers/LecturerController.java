package org.university.Controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.university.Services.CourseService;
import org.university.Services.CourseSubjectService;
import org.university.Services.SubjectService;
import org.university.dto.CourseDTO;
import org.university.dto.CourseSubjectDTO;
import org.university.dto.SubjectDTO;

import java.util.List;

@Path("lecturer")
public class LecturerController {
    @Inject
    CourseService courseService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getallcourses")
    public List<CourseDTO> getAllCourses(){
        return courseService.getAllCourses();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addcourse")
    public CourseDTO addCourse(CourseDTO dto){
        return courseService.createCourse(dto);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updatecourse/{id}")
    public CourseDTO updateCourse(@PathParam("id") long id,CourseDTO dto){
        return courseService.updateCourse(id,dto);
    }

    @DELETE
    @Path("/deletecourse/{id}")
    public void deleteCourse(@PathParam("id") long id){
        courseService.deleteCourse(id);
    }

    @Inject
    SubjectService subjectService;

    @GET
    @Path("/getallsubjects")
    public List<SubjectDTO> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addsubject")
    public SubjectDTO addSubject(SubjectDTO dto){
        return subjectService.createSubject(dto);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updatesubject/{id}")
    public SubjectDTO updateSubject(@PathParam("id") long id, SubjectDTO dto){
        return subjectService.updateSubject(id, dto);
    }

    @DELETE
    @Path("/deletesubject/{id}")
    public void deleteSubject(@PathParam("id") long id){
        subjectService.deleteSubject(id);
    }

    @Inject
    CourseSubjectService courseSubjectService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addsubjecttocourse")
    public CourseSubjectDTO addSubjectToCourse(CourseSubjectDTO dto){
        return courseSubjectService.addSubjectToCourse(dto);
    }
}
