package org.university.Mappers;

import org.mapstruct.Mapper;
import org.university.Models.Course_SubjectModel;
import org.university.dto.CourseSubjectDTO;

@Mapper(componentModel = "cdi")
public interface CourseSubjectMapper {

    default CourseSubjectDTO toDTO(Course_SubjectModel model){
        CourseSubjectDTO dto = new CourseSubjectDTO();
        dto.setCourseId(model.getCourseId().getCourseId());
        dto.setSubjectId(model.getSubjectId().getSubId());

        return dto;
    }
}
