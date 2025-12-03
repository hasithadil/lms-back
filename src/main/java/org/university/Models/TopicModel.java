package org.university.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "topic")
public class TopicModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(name = "order_index")
    private Integer orderIndex;  // To maintain topic order within subject

    @ManyToOne
    @JoinColumn(name = "sub_id")
    private SubjectModel subject;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentTopicProgressModel> studentProgress = new ArrayList<>();

}
