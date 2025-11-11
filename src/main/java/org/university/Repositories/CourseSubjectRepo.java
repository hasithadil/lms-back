package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.Course_SubjectModel;

import java.util.List;

@ApplicationScoped
public class CourseSubjectRepo implements PanacheRepository<Course_SubjectModel> {

    public List<Course_SubjectModel> findByCourseId(long courseId){
        return find("courseId.courseId",  courseId).list();
    }

}
