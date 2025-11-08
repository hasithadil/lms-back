package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_subject")
@IdClass(CourseSubjectId.class)
public class Course_SubjectModel  {

    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseModel courseId;

    @Id
    @ManyToOne
    @JoinColumn(name = "sub_id")
    private SubjectModel subjectId;

}
