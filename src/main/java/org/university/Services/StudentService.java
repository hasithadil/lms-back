//package org.university.Services;
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import jakarta.transaction.Transactional;
//import jakarta.ws.rs.NotFoundException;
//import org.university.Mappers.StudentMapper;
//import org.university.Mappers.StudentResponseMapper;
//import org.university.Models.Status;
//import org.university.Models.StudentModel;
//import org.university.Repositories.StudentRepo;
//import org.university.dto.StudentDTO;
//import org.university.dto.StudentResponseDTO;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@ApplicationScoped
//public class StudentService {
//
//    @Inject
//    StudentRepo studentRepo;
//
//    @Inject
//    StudentMapper studentMapper;
//
//    @Inject
//    StudentResponseMapper studentResponseMapper;
//
//    public List<StudentDTO> getAllStudents(){
//        List<StudentModel> students = studentRepo.list("ORDER BY status ASC");
//
//        return students.stream().map(studentMapper::toDTO).collect(Collectors.toList());
//    }
//
//    @Transactional
//    public StudentDTO createStudent(StudentDTO dto){
//        StudentModel student = studentMapper.toModel(dto);
//        studentRepo.persist(student);
//
//        return studentMapper.toDTO(student);
//    }
//
//    @Transactional
//    public StudentDTO updateStudent(Long id, StudentDTO dto){
//        StudentModel existingStudent = studentRepo.findById(id);
//
//        if (existingStudent == null){
//            throw new NotFoundException("Student not found");
//        }
//
//        existingStudent.setKc_id(dto.getKc_id());
//        existingStudent.setFirstName(dto.getFirstName());
//        existingStudent.setLastName(dto.getLastName());
//        existingStudent.setEmail(dto.getEmail());
//        existingStudent.setStatus(Status.ACTIVE);
//
//        return studentMapper.toDTO(existingStudent);
//    }
//
//    @Transactional
//    public void deleteStudent(long id){
//        StudentModel existingStudent = studentRepo.findById(id);
//
//        if (existingStudent == null){
//            throw new NotFoundException("Student not found");
//        }
//
//        existingStudent.setStatus(Status.INACTIVE);
//    }
//
//    public StudentResponseDTO getSpecficStudentDetails(Long id){
//        StudentModel student = studentRepo.findById(id);
//        if (student == null){
//            throw new NotFoundException("Student not found");
//        }
//
//        // Force fetch enrollments if they are lazy-loaded
//        student.getEnrollments().size();
//
//        return studentResponseMapper.toDto(student);
//    }
//}

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

        System.out.println("🔵 STEP 1: StudentService.createStudent() called");
        System.out.println("Email: " + dto.getEmail());
        System.out.println("FirstName: " + dto.getFirstName());
        System.out.println("LastName: " + dto.getLastName());
        // ============================================
        // CHANGED: Create user in Keycloak FIRST
        // ============================================
//        String keycloakId = keycloakService.createUser(
//                dto.getEmail(),          // Email
//                dto.getFirstName(),      // First name
//                dto.getLastName(),       // Last name
//                "STUDENT"                // Role (IMPORTANT!)
//        );

        String keycloakId = null;

        try {
            System.out.println("🔵 STEP 2: About to call KeycloakService.createUser()");

            keycloakId = keycloakService.createUser(
                    dto.getEmail(),          // Email
                    dto.getFirstName(),      // First name
                    dto.getLastName(),       // Last name
                    "STUDENT"                // Role - use YOUR actual role name
            );

            System.out.println("🔵 STEP 3: KeycloakService returned successfully");
            System.out.println("✅ keycloakId: " + keycloakId);

        } catch (Exception e) {
            System.err.println("❌❌❌ ERROR in KeycloakService.createUser() ❌❌❌");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException("Failed to create user in Keycloak: " + e.getMessage(), e);
        }

        System.out.println("keycloakId:"+keycloakId);


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
