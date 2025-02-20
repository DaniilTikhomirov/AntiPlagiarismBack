package com.plagiarism.AntiPlagiarismBack.mappers;

import com.plagiarism.AntiPlagiarismBack.dto.HomeWorkDto;
import com.plagiarism.AntiPlagiarismBack.models.HomeWork;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HomeWorkMapper {

    HomeWorkDto homeWorkToHomeWorkDto(HomeWork homeWork);

    List<HomeWorkDto> homeWorkListToHomeWorkDtoList(List<HomeWork> homeWorkList);
}
