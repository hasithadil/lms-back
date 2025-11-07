package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "lecturer")
public class LecturerModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lec_id")
    public Long lec_id;

    @Column(name = "kc_id", nullable = false, unique = true)
    public String kc_id;  // Keycloak user id (links login user)

    @Column(nullable = false, unique = true)
    public String email;

    @Column(name = "f_name", nullable = false)
    public String firstName;

    @Column(name = "l_name", nullable = false)
    public String lastName;

    @Column(nullable = false)
    public String status; // ACTIVE / INACTIVE
}
