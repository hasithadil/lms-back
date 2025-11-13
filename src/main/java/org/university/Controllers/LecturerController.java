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

    @Inject
    SubjectService subjectService;

    @Inject
    CourseSubjectService courseSubjectService;


    @GET
    @Path("/courses")
    public List<CourseDTO> getAllCourses(){
        return courseService.getAllCourses();
    }

    @POST
    @Path("/courses")
    public CourseDTO addCourse(CourseDTO dto){
        return courseService.createCourse(dto);
    }

    @PUT
    @Path("/course/{id}")
    public CourseDTO updateCourse(@PathParam("id") long id,CourseDTO dto){
        return courseService.updateCourse(id,dto);
    }

    @DELETE
    @Path("/course/{id}")
    public void deleteCourse(@PathParam("id") long id){
        courseService.deleteCourse(id);
    }


    @GET
    @Path("/subjects")
    public List<SubjectDTO> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    @POST
    @Path("/subjects")
    public SubjectDTO addSubject(SubjectDTO dto){
        return subjectService.createSubject(dto);
    }

    @PUT
    @Path("/subject/{id}")
    public SubjectDTO updateSubject(@PathParam("id") long id, SubjectDTO dto){
        return subjectService.updateSubject(id, dto);
    }

    @DELETE
    @Path("/subject/{id}")
    public void deleteSubject(@PathParam("id") long id){
        subjectService.deleteSubject(id);
    }


    @POST
    @Path("/subjecttocourse")
    public CourseSubjectDTO addSubjectToCourse(CourseSubjectDTO dto){
        return courseSubjectService.addSubjectToCourse(dto);
    }

    @GET
    @Path("/{course_id}/subjects")
    public List<SubjectDTO> getSubjectsByCourse(@PathParam("course_id") long courseId){
        return courseSubjectService.getSubjectsByCourse(courseId);
    }

    @DELETE
    @Path("course/{courseId}/subject/{subjectId}")
    public void deleteSubject(@PathParam("courseId") long courseId, @PathParam("subjectId") long subjectId){
        courseSubjectService.removeSubjectFromCourse(courseId, subjectId);
    }
}
