package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.university.Models.LecturerModel;
import org.university.dto.LecturerDTO;

@Mapper(componentModel = "cdi")
public interface LecturerMapper {
    LecturerMapper INSTANCE = Mappers.getMapper(LecturerMapper.class);

    // When converting DTO to Model for creation, ignore the ID
    @Mapping(target = "lec_id", ignore = true)
    LecturerModel toModel(LecturerDTO dto);

    LecturerDTO toDTO(LecturerModel model);
}
