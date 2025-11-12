package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lecturer")
public class LecturerModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lec_id")
    private Long lec_id;

    @Column(name = "kc_id", nullable = false, unique = true)
    private String kc_id;  // Keycloak user id (links login user)

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "f_name", nullable = false)
    private String firstName;

    @Column(name = "l_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String status; // ACTIVE / INACTIVE

    //reverse map to course
    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseModel> courses = new ArrayList<>();

    //reverse map to subject
    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectModel> subjects = new ArrayList<>();
}
