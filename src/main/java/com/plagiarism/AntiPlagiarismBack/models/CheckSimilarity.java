package com.plagiarism.AntiPlagiarismBack.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(
        name = "check_similarity",
        indexes = {
                @Index(name = "idx_student1_student2_homework", columnList = "student1_id, student2_id, home_work_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_student1_student2_homework", columnNames = {"student1_id", "student2_id", "home_work_id"})
        }
)@ToString
public class CheckSimilarity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE , fetch = FetchType.EAGER)
    @JoinColumn(name = "student1_id", nullable = false)
    private Student student1;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "student2_id", nullable = false)
    private Student student2;

    private double similarity;

    @ManyToOne()
    @JoinColumn(name = "home_work_id")
    private HomeWork homeWork;
}

