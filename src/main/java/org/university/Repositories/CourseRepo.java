package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.CourseModel;

@ApplicationScoped
public class CourseRepo implements PanacheRepository<CourseModel> {
}
