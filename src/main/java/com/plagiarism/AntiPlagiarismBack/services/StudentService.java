package com.plagiarism.AntiPlagiarismBack.services;

import com.plagiarism.AntiPlagiarismBack.dto.StudentDto;
import com.plagiarism.AntiPlagiarismBack.mappers.StudentMapper;
import com.plagiarism.AntiPlagiarismBack.models.Student;
import com.plagiarism.AntiPlagiarismBack.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public Student addStudent(StudentDto student) {
        return studentRepository.save(studentMapper.toStudent(student));
    }

    public Student addStudent(Student student){
        return studentRepository.save(student);
    }

    public List<StudentDto> getAllStudents() {
        return studentMapper.toStudentDtoList(studentRepository.findAll());
    }

    public Student getStudentById(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> getStudentsByName(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Student> addStudents(List<StudentDto> students) {
        return studentRepository.saveAll(studentMapper.toStudentList(students));
    }
}
