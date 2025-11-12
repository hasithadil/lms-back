package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.university.Models.CourseModel;
import org.university.Models.LecturerModel;
import org.university.Models.SubjectModel;
import org.university.dto.LecturerCourseSummaryDTO;
import org.university.dto.LecturerResponseDTO;
import org.university.dto.LecturerSubjectDTO;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface LecturerResponseMapper {

    @Mapping(source = "courses", target = "courses", qualifiedByName = "mapCourses")
    @Mapping(source = "subjects" , target = "subjects", qualifiedByName = "mapSubjects")
    @Mapping(source = ".", target = "name", qualifiedByName = "concatName")
    LecturerResponseDTO toDto(LecturerModel lecturer);

    @Named("concatName")
    default String concatName(LecturerModel lecturer) {
        return lecturer.getFirstName() + " " + lecturer.getLastName();
    }

    @Named("mapCourses")
    default List<LecturerCourseSummaryDTO> mapCourses(List<CourseModel> courses) {
        if (courses == null) {
            return List.of();
        }

        return courses.stream().map(c ->{
            LecturerCourseSummaryDTO dto = new LecturerCourseSummaryDTO();
            dto.setCourseId(c.getCourseId());
            dto.setName(c.getName());
            dto.setMaxStudent(c.getMaxStudent());

            return dto;
        }).
                collect(Collectors.toList());
    }

    @Named("mapSubjects")
    default List<LecturerSubjectDTO> mapSubjects(List<SubjectModel> subjects) {
        if (subjects == null) {
            return List.of();
        }

        return subjects.stream().map(s ->{
            LecturerSubjectDTO dto = new LecturerSubjectDTO();
            dto.setSubId(s.getSubId());
            dto.setSubjectName(s.getSubjectName());

            return dto;
        }).
                collect(Collectors.toList());
    }
}
