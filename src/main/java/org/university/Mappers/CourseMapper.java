package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.university.Models.CourseModel;
import org.university.dto.CourseDTO;

@Mapper(componentModel = "cdi")
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "lecturer.lec_id", target = "lecturerId")   // ✅ map model → dto
    CourseDTO toDto(CourseModel model);

    @Mapping(target = "courseId" , ignore = true)
    @Mapping(target = "lecturer", ignore = true)  // ✅ Set manually in service
    @Mapping(source = "lecturerId", target = "lecturer.lec_id")   // ✅ map dto → model
    CourseModel toModel(CourseDTO dto);
}
