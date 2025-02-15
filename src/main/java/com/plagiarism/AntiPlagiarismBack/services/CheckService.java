package com.plagiarism.AntiPlagiarismBack.services;

import com.plagiarism.AntiPlagiarismBack.dto.*;
import com.plagiarism.AntiPlagiarismBack.mappers.CheckMapper;
import com.plagiarism.AntiPlagiarismBack.models.CheckSimilarity;
import com.plagiarism.AntiPlagiarismBack.models.Student;
import com.plagiarism.AntiPlagiarismBack.repository.CheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CheckService {
    private final CheckRepository checkRepository;
    private final CheckMapper checkMapper;
    private final CalculateService calculateService;
    private final StudentService studentService;

    public CheckSimilarity findById(long id) {
        return checkRepository.findById(id).orElse(null);
    }


    public List<CheckSimilarity> addAll(List<AddCheckWithStudent> addChecks) {
        System.out.println(addChecks.toString());
        Set<AddCheckWithStudent> students = new HashSet<>(addChecks);
        List<CheckSimilarity> checkSimilarities = checkMapper.toCheckSimilarities(students.stream().toList());
        System.out.println(checkSimilarities.toString());
        return checkRepository.saveAll(checkSimilarities);
    }

    public List<CheckSimilarity> addAll(Set<CheckSimilarity> addChecks){
        return checkRepository.saveAll(addChecks);
    }

    public CheckSimilarity find(long student1Id, long student2Id, long homeWorkId){
        return checkRepository.findByStudent1IdAndStudent2IdAndHomeWorkId(student1Id, student2Id, homeWorkId).orElse(null);
    }


}
