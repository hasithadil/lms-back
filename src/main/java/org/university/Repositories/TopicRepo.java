package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.TopicModel;

import java.util.List;

@ApplicationScoped
public class TopicRepo implements PanacheRepositoryBase<TopicModel, Long> {
    public List<TopicModel> findBySubjectId(Long subjectId){
        return list("subject.subId = ?1 ORDER BY orderIndex ASC",subjectId);
    }
}
