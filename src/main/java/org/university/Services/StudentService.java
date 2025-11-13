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

    public List<StudentDTO> getAllStudents(){
        List<StudentModel> students = studentRepo.listAll();

        return students.stream().map(studentMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO dto){
        StudentModel student = studentMapper.toModel(dto);
        studentRepo.persist(student);

        return studentMapper.toDTO(student);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO dto){
        StudentModel existingStudent = studentRepo.findById(id);

        if (existingStudent == null){
            throw new NotFoundException("Student not found");
        }

        existingStudent.setKc_id(dto.getKc_id());
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

        existingStudent.setStatus(Status.INACTIVE);
    }

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
