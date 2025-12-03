package org.university.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "student_topic_progress")
public class StudentTopicProgressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentModel student;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private TopicModel topic;

    @Column(name = "is_completed" , nullable = false)
    private boolean isCompleted =false;
}
