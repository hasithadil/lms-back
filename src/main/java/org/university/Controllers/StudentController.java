package org.university.Controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.university.Repositories.EnrollmentRepo;
import org.university.Services.*;
import org.university.dto.*;

import java.util.List;

@Path("/student")
@RolesAllowed("STUDENT")
public class StudentController {
    @Inject
    EnrollmentService enrollmentService;

    @Inject
    StudentService studentService;

    @Inject
    CourseService courseService;

    @Inject
    TopicService topicService;

    @Inject
    StudentProgresService studentProgresService;

    @GET
    @Path("/students")
    public List<StudentDTO> getAllStudents(){
        return studentService.getAllStudents();
    }

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

    @GET
    @Path("/subject/{subjectId}/topics")
    public List<TopicDTO> getTopicsBySubject(@PathParam("subjectId") Long subjectId) {
        return topicService.getTopicsBySubject(subjectId);
    }

    @POST
    @Path("{studentId}/progress/mark")
    public TopicProgressDTO markProgress(@PathParam("studentId") Long studentId,TopicProgressDTO dto){
        return studentProgresService.markTopicProgress(studentId,dto);
    }

    @GET
    @Path("/{studentId}/progress/subject/{subjectId}")
    public SubjectProgressDTO getProgressInSubject(
            @PathParam("studentId") Long studentId,
            @PathParam("subjectId") Long subjectId) {
        return studentProgresService.getStudentProgressInSubject(studentId, subjectId);
    }

    @GET
    @Path("/{studentId}/progress/course/{courseId}")
    public CourseProgressDTO getProgressInCourse(
            @PathParam("studentId") Long studentId,
            @PathParam("courseId") Long courseId) {
        return studentProgresService.getStudentProgressInCourse(studentId, courseId);
    }

    @GET
    @Path("/{studentId}/progress/overall")
    public StudentOverallProgressDTO getOverallProgress(@PathParam("studentId") Long studentId) {
        return studentProgresService.getStudentOverallProgress(studentId);
    }

}
