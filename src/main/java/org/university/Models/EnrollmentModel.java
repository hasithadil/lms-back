package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "enrollment")
public class EnrollmentModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "e_id")
    private long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "s_id", nullable = false)
    private StudentModel student;  // Student reference (FK)

    @ManyToOne
    @JoinColumn(name = "c_id", nullable = false)
    private  CourseModel course;  // Course reference (FK)

    @Column(name = "e_date", nullable = false)
    private LocalDate enrollmentDate; // Enrollment date

}
