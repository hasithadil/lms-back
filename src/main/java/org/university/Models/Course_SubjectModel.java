package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "course_subject")
@IdClass(CourseSubjectId.class)
public class Course_SubjectModel extends PanacheEntityBase {

    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    public CourseModel courseId;

    @Id
    @ManyToOne
    @JoinColumn(name = "sub_id")
    public SubjectModel subjectId;

}
