package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subject")
public class SubjectModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    private long subId;

    @Column(name = "sub_name" , nullable = false)
    private String subjectName;

    @ManyToOne
    @JoinColumn(name = "lec_id")
    private LecturerModel lecturer;
}
