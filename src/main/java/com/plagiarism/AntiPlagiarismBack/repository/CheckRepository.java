package com.plagiarism.AntiPlagiarismBack.repository;

import com.plagiarism.AntiPlagiarismBack.models.CheckSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckRepository extends JpaRepository<CheckSimilarity, Long> {
    Optional<CheckSimilarity> findByStudent1IdAndStudent2IdAndHomeWorkId(long student1_id, long student2_id, long homeWork_id);
}
