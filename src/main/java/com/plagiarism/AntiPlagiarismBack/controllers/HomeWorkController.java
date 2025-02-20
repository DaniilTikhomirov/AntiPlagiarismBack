package com.plagiarism.AntiPlagiarismBack.controllers;

import com.plagiarism.AntiPlagiarismBack.dto.HomeWorkDto;
import com.plagiarism.AntiPlagiarismBack.services.HomeWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("homework")
@RequiredArgsConstructor
public class HomeWorkController {
    private final HomeWorkService homeWorkService;

    @GetMapping()
    public ResponseEntity<List<HomeWorkDto>> getHomeWork() {
        return ResponseEntity.ok(homeWorkService.findAll());
    }
}
