package com.plagiarism.AntiPlagiarismBack.controllers;

import com.plagiarism.AntiPlagiarismBack.dto.StudentDto;
import com.plagiarism.AntiPlagiarismBack.models.Student;
import com.plagiarism.AntiPlagiarismBack.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping()
    public ResponseEntity<Student> addStudent(@RequestBody StudentDto student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    @GetMapping()
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
}
