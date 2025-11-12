package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.Course_SubjectModel;

import java.util.List;

@ApplicationScoped
public class CourseSubjectRepo implements PanacheRepositoryBase<Course_SubjectModel, Long> {

    public List<Course_SubjectModel> findByCourseId(long courseId){
        return find("courseId.courseId",  courseId).list();
    }

}
