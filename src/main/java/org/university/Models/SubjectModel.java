package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "subject")
public class SubjectModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    public long subId;

    @Column(name = "sub_name" , nullable = false)
    public String subjectName;

    @ManyToOne
    @JoinColumn(name = "lec_id")
    public LecturerModel lecturer;
}
