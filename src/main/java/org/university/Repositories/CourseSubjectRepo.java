package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.Course_SubjectModel;

@ApplicationScoped
public class CourseSubjectRepo implements PanacheRepository<Course_SubjectModel> {
}
