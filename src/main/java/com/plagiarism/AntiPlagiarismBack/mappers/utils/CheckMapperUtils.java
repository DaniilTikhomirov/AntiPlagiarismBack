package com.plagiarism.AntiPlagiarismBack.mappers.utils;

import com.plagiarism.AntiPlagiarismBack.dto.StudentDto;
import com.plagiarism.AntiPlagiarismBack.mappers.StudentMapper;
import com.plagiarism.AntiPlagiarismBack.models.Student;
import com.plagiarism.AntiPlagiarismBack.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Named("CheckMapperUtils")
@Component
@RequiredArgsConstructor
public class CheckMapperUtils {
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @Named("getStudentById")
    public Student getStudentById(long id) {
        return studentService.getStudentById(id);
    }

    @Named("convertStudents")
    public StudentDto convertStudents(Student student) {
        return studentMapper.toStudentDto(student);
    }

    @Named("convertToStudent")
    public Student convertToStudent(StudentDto studentDto) {
        List<Student> students = studentService.getStudentsByName(studentDto.getFirstName(), studentDto.getLastName());
        if (students.isEmpty()) {
            return studentMapper.toStudent(studentDto);
        }
        return students.get(0);
    }
}
