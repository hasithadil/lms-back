package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.university.Mappers.LecturerMapper;
import org.university.Models.LecturerModel;
import org.university.Repositories.LecturerRepo;
import org.university.dto.LecturerDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LecturerService {

    @Inject
    LecturerRepo lecturerRepo;

    @Inject
    LecturerMapper lecturerMapper;

    public List<LecturerDTO> getAllLectures(){
        List<LecturerModel> lecturers = lecturerRepo.listAll();

        return lecturers.stream().map(lecturerMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public LecturerDTO createLecturer(LecturerDTO dto){
        LecturerModel lecturer = lecturerMapper.toModel(dto);
        lecturerRepo.persist(lecturer);

        return lecturerMapper.toDTO(lecturer);
    }

    @Transactional
    public LecturerDTO updatelecturer(long id, LecturerDTO dto){
        LecturerModel lecturer = lecturerRepo.findById(id);

        if(lecturer == null){
            throw new NotFoundException("Lecturer with id " + id + " not found");
        }

        lecturer.setKc_id(dto.getKc_id());
        lecturer.setEmail(dto.getEmail());
        lecturer.setFirstName(dto.getFirstName());
        lecturer.setLastName(dto.getLastName());

        return lecturerMapper.toDTO(lecturer);
    }

    @Transactional
    public void deleteLecturer(long id){
        LecturerModel lecturer = lecturerRepo.findById(id);

        if(lecturer == null){
            throw new NotFoundException("Lecturer with id " + id + " not found");
        }

        lecturer.setStatus("inactive");
    }
}
