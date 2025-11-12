package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.LecturerModel;

@ApplicationScoped
public class LecturerRepo implements PanacheRepositoryBase<LecturerModel, Long> {
}
