package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class StudentModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    public Long s_id;  // primary key

    @Column(name = "kc_id" , nullable = false, unique = true)
    public String kc_id;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(name = "f_name", nullable = false)
    public String firstName;

    @Column(name = "l_name", nullable = false)
    public String lastName;

    @Column(nullable = false)
    public String status;  // e.g., "ACTIVE", "INACTIVE"

}
