package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.university.Models.CourseModel;
import org.university.Models.Course_SubjectModel;
import org.university.Models.LecturerModel;
import org.university.dto.CourseResponseDTO;
import org.university.dto.SubjectResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface CourseResponseMapper {

    @Mapping(source = "lecturer", target = "lecturerName", qualifiedByName = "mapLecturerName")
    @Mapping(source = "courseSubjects", target = "subjects", qualifiedByName = "mapSubjects")
    CourseResponseDTO toDto(CourseModel course);

    @Named("mapLecturerName")
    default String mapLecturerName(LecturerModel lecturer) {
        if (lecturer == null) {
            return null;
        }

        return lecturer.getFirstName() + " " + lecturer.getLastName();
    }

    @Named("mapSubjects")
    default List<SubjectResponseDTO> mapSubjects(List<Course_SubjectModel> courseSubjects) {
        if (courseSubjects == null) {
            return List.of();
        }

        return courseSubjects.stream().map(cs->{
            SubjectResponseDTO dto = new SubjectResponseDTO();
            dto.setSubjectId(cs.getSubjectId().getSubId());
            dto.setSubjectName(cs.getSubjectId().getSubjectName());

            return dto;
        }).
                collect(Collectors.toList());
    }
}
