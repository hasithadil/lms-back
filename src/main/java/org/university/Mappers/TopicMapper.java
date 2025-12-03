package org.university.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.university.Models.TopicModel;
import org.university.dto.TopicDTO;

@Mapper(componentModel = "cdi")
public interface TopicMapper {

    TopicDTO toDto(TopicModel model);

    @Mapping(source = "subjectId", target = "subject.subId")
    TopicModel toModel(TopicDTO dto);
}
