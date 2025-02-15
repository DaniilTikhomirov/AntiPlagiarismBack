package com.plagiarism.AntiPlagiarismBack.controllers;

import com.plagiarism.AntiPlagiarismBack.services.CalculateService;
import com.plagiarism.AntiPlagiarismBack.services.GitHubProjectsParser;
import com.plagiarism.AntiPlagiarismBack.services.LevenshteinSimilarity;
import com.plagiarism.AntiPlagiarismBack.services.Shnigle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("plagiarism")
public class PlagiarismController {
    private final GitHubProjectsParser gitHubProjectsParser;
    private final LevenshteinSimilarity levenshteinSimilarity;
    private final Shnigle shnigle;
    private final CalculateService calculateService;

    public PlagiarismController(GitHubProjectsParser gitHubProjectsParser,
                                LevenshteinSimilarity levenshteinSimilarity,
                                Shnigle shnigle,
                                CalculateService calculateService) {

        this.gitHubProjectsParser = gitHubProjectsParser;
        this.levenshteinSimilarity = levenshteinSimilarity;
        this.shnigle = shnigle;
        this.calculateService = calculateService;
    }


}
