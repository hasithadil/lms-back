package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.university.Mappers.SubjectMapper;
import org.university.Models.LecturerModel;
import org.university.Models.SubjectModel;
import org.university.Repositories.LecturerRepo;
import org.university.Repositories.SubjectRepo;
import org.university.dto.SubjectDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SubjectService {
    @Inject
    SubjectRepo subjectRepo;

    @Inject
    SubjectMapper subjectMapper;

    @Inject
    LecturerRepo lecturerRepo;

    public List<SubjectDTO> getAllSubjects(){
        List<SubjectModel> subjects = subjectRepo.listAll();

        return subjects.stream().map(subjectMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public SubjectDTO createSubject(SubjectDTO dto){
        var lecturer = lecturerRepo.findById(dto.getLecturerId());

        if(lecturer == null){
            throw new NotFoundException("Lecturer not found");
        }

        SubjectModel subject = subjectMapper.toModel(dto);
        subject.setLecturer(lecturer);

        subjectRepo.persist(subject);
        return subjectMapper.toDTO(subject);
    }

    @Transactional
    public SubjectDTO updateSubject(long id, SubjectDTO dto){
        SubjectModel subject = subjectRepo.findById(id);

        if(subject == null){
            throw new NotFoundException("Subject not found");
        }

        LecturerModel lecturer = lecturerRepo.findById(dto.getLecturerId());

        if(lecturer == null){
            throw new NotFoundException("Lecturer not found");
        }

        subject.setLecturer(lecturer);
        subject.setSubjectName(dto.getSubjectName());

        return subjectMapper.toDTO(subject);
    }

    @Transactional
    public void deleteSubject(long id){
        SubjectModel subject = subjectRepo.findById(id);

        if(subject == null){
            throw new NotFoundException("Subject not found");
        }

        subjectRepo.delete(subject);
    }
}
