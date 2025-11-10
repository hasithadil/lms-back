package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.university.Models.SubjectModel;
import org.university.dto.SubjectDTO;

@Mapper(componentModel = "cdi")
public interface SubjectMapper {

    @Mapping(target = "lecturer"  , ignore = true)
    @Mapping(source = "lecturerId", target = "lecturer.lec_id")   // ✅ map dto → model
    SubjectModel toModel(SubjectDTO dto);

    @Mapping(source = "lecturer.lec_id" , target = "lecturerId")
    SubjectDTO toDTO(SubjectModel model);
}
