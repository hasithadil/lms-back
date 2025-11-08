package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.university.Models.LecturerModel;
import org.university.dto.LecturerDTO;

@Mapper(componentModel = "cdi")
public interface LecturerMapper {
    LecturerMapper INSTANCE = Mappers.getMapper(LecturerMapper.class);

    LecturerModel toModel(LecturerDTO dto);
    LecturerDTO toDTO(LecturerModel model);
}
