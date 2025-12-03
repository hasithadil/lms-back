package org.university.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subject")
public class SubjectModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    private Long subId;

    @Column(name = "sub_name" , nullable = false)
    private String subjectName;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicModel> topics = new ArrayList<>();  // By initializing it with = new ArrayList<>(); ensure it's always an empty but usable list from the start

    @ManyToOne
    @JoinColumn(name = "lec_id")
    private LecturerModel lecturer;

    @OneToMany(mappedBy = "subjectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course_SubjectModel> courseSubjects = new ArrayList<>();
}
