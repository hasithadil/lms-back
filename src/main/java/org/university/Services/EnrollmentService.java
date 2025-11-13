package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.university.Mappers.EnrollmentMapper;
import org.university.Models.CourseModel;
import org.university.Models.EnrollmentModel;
import org.university.Models.StudentModel;
import org.university.Repositories.CourseRepo;
import org.university.Repositories.EnrollmentRepo;
import org.university.Repositories.StudentRepo;
import org.university.dto.EnrollmentDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EnrollmentService {

    @Inject
    EnrollmentMapper enrollmentMapper;

    @Inject
    EnrollmentRepo enrollmentRepo;

    @Inject
    StudentRepo studentRepo;

    @Inject
    CourseRepo courseRepo;

    @Transactional
    public EnrollmentDTO enrollStudent(EnrollmentDTO dto) {
        StudentModel student = studentRepo.findById(dto.getStudentId());
        if (student == null) {
            throw new NotFoundException("Student not found");
        }

        CourseModel course = courseRepo.findById(dto.getCourseId());
        if (course == null) {
            throw new NotFoundException("Course not found");
        }

        //check maximum students
        long count = enrollmentRepo.count("course.courseId", dto.getCourseId());
        if (count >= course.getMaxStudent()){
            throw new IllegalStateException("Course max student");
        }

        boolean alreadyEnrolled = enrollmentRepo
                .count("student.s_id = ?1 and course.courseId = ?2", dto.getStudentId(), dto.getCourseId()) > 0;

        if (alreadyEnrolled) {
            throw new IllegalStateException("Student already enrolled in this course");
        }


        EnrollmentModel enrollment = new EnrollmentModel();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        enrollmentRepo.persist(enrollment);

        return enrollmentMapper.toDto(enrollment);
    }

    public List<EnrollmentDTO> getEnrollmentsByStudent(long studentId){
        return enrollmentRepo.find("student.s_id", studentId).stream().map(enrollmentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void unenrollStudent(long studentId, long courseId){
        EnrollmentModel enrollment = enrollmentRepo.find("student.s_id = ?1 AND course.courseId = ?2", studentId,courseId).firstResult();

        if (enrollment == null) {
            throw new NotFoundException("enrollment not found");
        }

        enrollmentRepo.delete(enrollment);
    }
}
