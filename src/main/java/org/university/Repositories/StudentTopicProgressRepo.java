package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.StudentTopicProgressModel;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StudentTopicProgressRepo implements PanacheRepositoryBase<StudentTopicProgressModel,Long> {
    // Optional<T> in Java is used to represent a value that may or may not exist.
    public Optional<StudentTopicProgressModel> findByStudentAndTopic(Long studentId, Long topicId) {
        return find("student.s_id = ?1 AND topic.topicId = ?2", studentId, topicId).firstResultOptional();
    }

    public List<StudentTopicProgressModel> findByStudentAndSubject(Long studentId, Long subjectId) {
        return list("student.s_id = ?1 AND topic.subject.subId = ?2", studentId, subjectId);
    }

    public List<StudentTopicProgressModel> findByStudentId(Long studentId) {
        return list("student.s_id", studentId);
    }

//    SELECT COUNT(*)
//    FROM topic t
//    JOIN subject s ON t.subject_id = s.sub_id
//    JOIN student st ON t.student_id = st.s_id
//    WHERE st.s_id = :studentId
//    AND s.sub_id = :subjectId
//    AND t.is_complete = TRUE;
    public long countCompletedBysStudentAndSubject(Long studentId, Long subjectId){
        return count("student.s_id = ?1 AND topic.subject.subId = ?2 AND isComplete = true", studentId, subjectId);
    }
}
