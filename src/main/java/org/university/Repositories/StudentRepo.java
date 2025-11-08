package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.StudentModel;

@ApplicationScoped
public class StudentRepo implements PanacheRepository<StudentModel> {
}
