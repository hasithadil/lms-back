package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.StudentModel;

@ApplicationScoped
public class StudentRepo implements PanacheRepositoryBase<StudentModel, Long> {
}
