package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "student")
public class StudentModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long s_id;  // primary key

    @Column(name = "kc_id" , nullable = false, unique = true)
    private String kc_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "f_name", nullable = false)
    private String firstName;

    @Column(name = "l_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String status;  // e.g., "ACTIVE", "INACTIVE"

    // reverse mapping to enrollements
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "test")
    private List<EnrollmentModel> enrollments = new ArrayList<>();

}
