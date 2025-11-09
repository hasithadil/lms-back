package org.university.Controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.university.Services.CourseService;
import org.university.dto.CourseDTO;

@Path("lecturer")
public class LecturerController {
    @Inject
    CourseService courseService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addcourse")
    public CourseDTO addCourse(CourseDTO dto){
        return courseService.createCourse(dto);
    }
}
