package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.EnrollmentModel;

@ApplicationScoped
public class EnrollmentRepo implements PanacheRepository<EnrollmentModel> {
}
