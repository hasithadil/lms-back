package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.university.Models.EnrollmentModel;
import org.university.dto.EnrollmentDTO;

@Mapper(componentModel = "cdi")
public interface EnrollmentMapper {

    @Mapping(source = "student.s_id" ,target = "studentId")
    @Mapping(source = "course.courseId" , target = "courseId")
    @Mapping(source = "enrollmentDate", target = "enrollmentDate")
    EnrollmentDTO toDto(EnrollmentModel model);

    @Mapping(source = "studentId" ,target = "student.s_id")
    @Mapping(source = "courseId" , target = "course.courseId")
    EnrollmentModel toModel(EnrollmentDTO dto);
}
