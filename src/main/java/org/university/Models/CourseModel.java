package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "course")
public class CourseModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private long courseId;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_student", nullable = false)
    private Integer maxStudent;

    @ManyToOne
    @JoinColumn(name = "lec_id")
    private LecturerModel lecturer; // Reference to Lecturer (FK)
}
