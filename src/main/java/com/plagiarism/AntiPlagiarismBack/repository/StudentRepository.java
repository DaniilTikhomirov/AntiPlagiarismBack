package com.plagiarism.AntiPlagiarismBack.repository;

import com.plagiarism.AntiPlagiarismBack.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFirstNameAndLastName(String firstName, String lastName);
}
