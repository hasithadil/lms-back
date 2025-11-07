package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
public class EnrollmentModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "e_id")
    public long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "s_id", nullable = false)
    public StudentModel student;  // Student reference (FK)

    @ManyToOne
    @JoinColumn(name = "c_id", nullable = false)
    public CourseModel course;  // Course reference (FK)

    @Column(name = "e_date", nullable = false)
    public LocalDate enrollmentDate; // Enrollment date

}
