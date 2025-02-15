package com.plagiarism.AntiPlagiarismBack.controllers;

import com.plagiarism.AntiPlagiarismBack.dto.AddCheckWithStudent;
import com.plagiarism.AntiPlagiarismBack.models.CheckSimilarity;
import com.plagiarism.AntiPlagiarismBack.services.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("check")
@RequiredArgsConstructor
public class CheckController {
    private final CheckService checkService;


    @PostMapping("test2")
    public ResponseEntity<List<CheckSimilarity>> addCheck2(@RequestBody List<AddCheckWithStudent> addCheckWithStudents) {
        return ResponseEntity.ok(checkService.addAll(addCheckWithStudents));
    }
}
