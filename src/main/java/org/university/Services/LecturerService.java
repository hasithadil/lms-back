package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.university.Mappers.LecturerMapper;
import org.university.Mappers.LecturerResponseMapper;
import org.university.Models.LecturerModel;
import org.university.Models.Status;
import org.university.Repositories.LecturerRepo;
import org.university.dto.LecturerDTO;
import org.university.dto.LecturerResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LecturerService {

    @Inject
    LecturerRepo lecturerRepo;

    @Inject
    LecturerMapper lecturerMapper;

    @Inject
    LecturerResponseMapper lecturerResponseMapper;

    // ============================================
    // NEW: Inject KeycloakService
    // ============================================
    @Inject
    KeycloakService keycloakService;

    // No changes needed here
    public List<LecturerDTO> getAllLectures(){
        List<LecturerModel> lecturers = lecturerRepo.list("ORDER BY status ASC");
        return lecturers.stream().map(lecturerMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public LecturerDTO createLecturer(LecturerDTO dto){
        // ============================================
        // CHANGED: Create user in Keycloak FIRST
        // ============================================
        String keycloakId = keycloakService.createUser(
                dto.getEmail(),          // Email
                dto.getFirstName(),      // First name
                dto.getLastName(),       // Last name
                "LECTURER"               // Role (IMPORTANT!)
        );

        // ============================================
        // CHANGED: Set Keycloak ID in lecturer model
        // ============================================
        LecturerModel lecturer = lecturerMapper.toModel(dto);
        lecturer.setKc_id(keycloakId);       // Link to Keycloak user
        lecturer.setStatus(Status.ACTIVE);   // Set as active

        lecturerRepo.persist(lecturer);

        return lecturerMapper.toDTO(lecturer);
    }

    @Transactional
    public LecturerDTO updatelecturer(long id, LecturerDTO dto){
        LecturerModel lecturer = lecturerRepo.findById(id);

        if(lecturer == null){
            throw new NotFoundException("Lecturer with id " + id + " not found");
        }

        // ============================================
        // CHANGED: Update user in Keycloak
        // ============================================
        keycloakService.updateUser(
                lecturer.getKc_id(),     // Keycloak ID
                dto.getEmail(),          // New email
                dto.getFirstName(),      // New first name
                dto.getLastName()        // New last name
        );

        // Update in database
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

        // ============================================
        // CHANGED: Disable user in Keycloak (soft delete)
        // ============================================
        keycloakService.disableUser(lecturer.getKc_id());

        // Mark as inactive in database
        lecturer.setStatus(Status.INACTIVE);
    }

    @Transactional
    public void reactivateLecturer(long id) {
        LecturerModel lecturer = lecturerRepo.findById(id);

        if (lecturer == null) {
            throw new NotFoundException("Lecturer not found");
        }

        // Enable in Keycloak
        keycloakService.enableUser(lecturer.getKc_id());

        // Update DB
        lecturer.setStatus(Status.ACTIVE);
    }


    // No changes needed here
    public LecturerResponseDTO getLecturerDetails(Long id){
        LecturerModel lecturer = lecturerRepo.findById(id);
        if(lecturer == null){
            throw new NotFoundException("Lecturer with id " + id + " not found");
        }

        lecturer.getCourses().size();
        lecturer.getSubjects().size();

        return lecturerResponseMapper.toDto(lecturer);
    }
}
