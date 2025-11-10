package org.university.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.university.Models.SubjectModel;

@ApplicationScoped
public class SubjectRepo implements PanacheRepository<SubjectModel> {
}
