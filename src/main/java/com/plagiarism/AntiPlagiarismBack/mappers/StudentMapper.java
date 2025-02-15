package com.plagiarism.AntiPlagiarismBack.mappers;

import com.plagiarism.AntiPlagiarismBack.dto.StudentDto;
import com.plagiarism.AntiPlagiarismBack.models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "id", ignore = true)
    Student toStudent(StudentDto studentDto);

    StudentDto toStudentDto(Student student);

    List<StudentDto> toStudentDtoList(List<Student> students);
    List<Student> toStudentList(List<StudentDto> studentDtos);

}
