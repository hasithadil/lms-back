package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.university.Mappers.StudentMapper;
import org.university.Models.StudentModel;
import org.university.Repositories.StudentRepo;
import org.university.dto.StudentDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StudentService {

    @Inject
    StudentRepo studentRepo;

    @Inject
    StudentMapper studentMapper;

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
}
