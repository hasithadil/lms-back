package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.university.Models.StudentModel;
import org.university.dto.StudentDTO;

@Mapper(componentModel = "cdi")
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    // When converting Model to DTO, include the ID
    StudentDTO toDTO(StudentModel entity);

    // When converting DTO to Model for creation, ignore the ID
    @Mapping(target = "s_id", ignore = true)
    StudentModel toModel(StudentDTO dto);
}
