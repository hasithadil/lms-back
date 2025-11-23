package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.university.Mappers.StudentMapper;
import org.university.Mappers.StudentResponseMapper;
import org.university.Models.Status;
import org.university.Models.StudentModel;
import org.university.Repositories.StudentRepo;
import org.university.dto.StudentDTO;
import org.university.dto.StudentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StudentService {

    @Inject
    StudentRepo studentRepo;

    @Inject
    StudentMapper studentMapper;

    @Inject
    StudentResponseMapper studentResponseMapper;

    // ============================================
    // NEW: Inject KeycloakService
    // ============================================
    @Inject
    KeycloakService keycloakService;

    // No changes needed here
    public List<StudentDTO> getAllStudents(){
        List<StudentModel> students = studentRepo.list("ORDER BY status ASC");
        return students.stream().map(studentMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO dto){

        String keycloakId = null;

        try {

            keycloakId = keycloakService.createUser(
                    dto.getEmail(),          // Email
                    dto.getFirstName(),      // First name
                    dto.getLastName(),       // Last name
                    "STUDENT"                // Role - use YOUR actual role name
            );


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create user in Keycloak: " + e.getMessage(), e);
        }


        // ============================================
        // CHANGED: Set Keycloak ID in student model
        // ============================================
        StudentModel student = studentMapper.toModel(dto);
        student.setKc_id(keycloakId);        // Link to Keycloak user
        student.setStatus(Status.ACTIVE);    // Set as active

        studentRepo.persist(student);

        return studentMapper.toDTO(student);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO dto){
        StudentModel existingStudent = studentRepo.findById(id);

        if (existingStudent == null){
            throw new NotFoundException("Student not found");
        }

        // ============================================
        // CHANGED: Update user in Keycloak
        // ============================================
        keycloakService.updateUser(
                existingStudent.getKc_id(),  // Keycloak ID
                dto.getEmail(),              // New email
                dto.getFirstName(),          // New first name
                dto.getLastName()            // New last name
        );

        // Update in database
        existingStudent.setFirstName(dto.getFirstName());
        existingStudent.setLastName(dto.getLastName());
        existingStudent.setEmail(dto.getEmail());
        existingStudent.setStatus(Status.ACTIVE);

        return studentMapper.toDTO(existingStudent);
    }

    @Transactional
    public void deleteStudent(long id){
        StudentModel existingStudent = studentRepo.findById(id);

        if (existingStudent == null){
            throw new NotFoundException("Student not found");
        }

        // ============================================
        // CHANGED: Disable user in Keycloak (soft delete)
        // ============================================
        keycloakService.disableUser(existingStudent.getKc_id());

        // Mark as inactive in database
        existingStudent.setStatus(Status.INACTIVE);
    }

    @Transactional
    public void reactivateStudent(long id) {
        StudentModel student = studentRepo.findById(id);

        if (student == null) {
            throw new NotFoundException("Student not found");
        }

        // Enable in Keycloak
        keycloakService.enableUser(student.getKc_id());

        // Update database
        student.setStatus(Status.ACTIVE);
    }


    // No changes needed here
    public StudentResponseDTO getSpecficStudentDetails(Long id){
        StudentModel student = studentRepo.findById(id);
        if (student == null){
            throw new NotFoundException("Student not found");
        }

        // Force fetch enrollments if they are lazy-loaded
        student.getEnrollments().size();

        return studentResponseMapper.toDto(student);
    }
}
