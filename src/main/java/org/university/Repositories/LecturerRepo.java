package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.LecturerModel;

@ApplicationScoped
public class LecturerRepo implements PanacheRepository<LecturerModel> {
}
