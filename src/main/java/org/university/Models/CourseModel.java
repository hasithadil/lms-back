package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "course")
public class CourseModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_student", nullable = false)
    private Integer maxStudent;

    @ManyToOne
    @JoinColumn(name = "lec_id")
    private LecturerModel lecturer; // Reference to Lecturer (FK)

    //reverse map to enrollments
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnrollmentModel> enrollments = new ArrayList<>();

    //reverse map to course_subject
    @OneToMany(mappedBy = "courseId" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course_SubjectModel> courseSubjects = new ArrayList<>();
}
