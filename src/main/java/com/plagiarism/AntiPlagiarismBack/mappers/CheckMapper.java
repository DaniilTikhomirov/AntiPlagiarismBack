package com.plagiarism.AntiPlagiarismBack.mappers;

import com.plagiarism.AntiPlagiarismBack.dto.AddCheckWithStudent;
import com.plagiarism.AntiPlagiarismBack.mappers.utils.CheckMapperUtils;
import com.plagiarism.AntiPlagiarismBack.models.CheckSimilarity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
            uses = {CheckMapperUtils.class})
@Component
public interface CheckMapper {


    @Mappings({
            @Mapping(target = "student1", qualifiedByName = "getStudentById", source = "student1_id"),
            @Mapping(target = "student2", qualifiedByName = "getStudentById", source = "student2_id")
    })
    CheckSimilarity toCheckSimilarity(AddCheckWithStudent addCheckWithStudent);

    List<CheckSimilarity> toCheckSimilarities(List<AddCheckWithStudent> addCheckWithStudents);
}
