package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class CourseModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    public long courseId;

    @Column(nullable = false)
    public String name;

    @Column(name = "max_student", nullable = false)
    public Integer maxStudent;

    @ManyToOne
    @JoinColumn(name = "lec_id")
    public LecturerModel lecturer; // Reference to Lecturer (FK)
}
