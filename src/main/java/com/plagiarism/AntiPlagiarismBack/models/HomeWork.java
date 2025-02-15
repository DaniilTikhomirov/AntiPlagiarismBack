package com.plagiarism.AntiPlagiarismBack.models;

import com.plagiarism.AntiPlagiarismBack.enums.HomeWorkType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "home_work")
public class HomeWork {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private HomeWorkType type;

    @Column(nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "homeWork", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CheckSimilarity> checkSimilarities;
}
