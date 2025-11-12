package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.university.Models.EnrollmentModel;
import org.university.Models.StudentModel;
import org.university.dto.CourseSummaryDTO;
import org.university.dto.StudentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface StudentResponseMapper {

    // to concanate name @Mapping(target = "name", expression = "java(student.getFirstName()+\"\" + student.getLastName())")
    @Mapping(source = "s_id", target = "id")
    @Mapping(source = "enrollments", target = "enrollments", qualifiedByName = "mapEnrollments")
    @Mapping(source = ".", target = "name", qualifiedByName = "concatName")
    StudentResponseDTO toDto(StudentModel student);

    // '.' tells MapStruct to pass the whole object into this method
    @Named("concatName")
    default String concatName(StudentModel student) {
        return student.getFirstName() + " " + student.getLastName();
    }

    @Named("mapEnrollments")
    default List<CourseSummaryDTO> mapEnrollments(List<EnrollmentModel> enrollments) {
        if (enrollments == null) {
            return List.of();
        }

        return enrollments.stream().map(e -> {
            CourseSummaryDTO dto = new CourseSummaryDTO();
            dto.setCourseId(e.getCourse().getCourseId());
            dto.setCourseName(e.getCourse().getName());

            return dto;
        }).
                collect(Collectors.toList());
    }
}
