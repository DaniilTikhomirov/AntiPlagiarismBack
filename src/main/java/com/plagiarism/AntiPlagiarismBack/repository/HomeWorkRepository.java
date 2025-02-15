package com.plagiarism.AntiPlagiarismBack.repository;

import com.plagiarism.AntiPlagiarismBack.models.HomeWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeWorkRepository extends JpaRepository<HomeWork, Long> {
    Optional<HomeWork> findByTitle(String title);

}
