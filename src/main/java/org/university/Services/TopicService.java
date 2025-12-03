package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.university.Mappers.TopicMapper;
import org.university.Models.SubjectModel;
import org.university.Models.TopicModel;
import org.university.Repositories.SubjectRepo;
import org.university.Repositories.TopicRepo;
import org.university.dto.TopicDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TopicService {
    @Inject
    TopicRepo topicRepo;

    @Inject
    SubjectRepo subjectRepo;

    @Inject
    TopicMapper topicMapper;

    public List<TopicDTO> getAllTopics(){
        return topicRepo.listAll().stream()
                .map(topicMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TopicDTO> getTopicsBySubject(Long subjectId){
        return topicRepo.findBySubjectId(subjectId).stream()
                .map(topicMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopicDTO createTopic(Long subjectId,TopicDTO dto){
        SubjectModel subject = subjectRepo.findById(subjectId);

        if(subject == null){
            throw new NotFoundException("Subject not found");
        }

        TopicModel topic = new TopicModel();
        topic.setSubject(subject);
        topic.setTopicName(dto.getTopicName());
        topic.setOrderIndex(dto.getOrderIndex());

        topicRepo.persist(topic);

        return topicMapper.toDto(topic);
    }
}
