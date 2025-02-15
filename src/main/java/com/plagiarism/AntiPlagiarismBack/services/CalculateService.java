package com.plagiarism.AntiPlagiarismBack.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculateService {
    private final LevenshteinSimilarity levenshteinSimilarity;
    private final GitHubProjectsParser gitHubProjectsParser;
    private final Shnigle shnigle;

    public double calculate(String link1, String link2) throws IOException, InterruptedException {
        String code1;
        String code2;
        if(link1.startsWith("https://github.com/")) {
            code1 = gitHubProjectsParser.getRepoTreeByLink(link1);

        }else {
            code1 = link1;
        }

        if(link2.startsWith("https://github.com/")) {
            code2 = gitHubProjectsParser.getRepoTreeByLink(link2);
        }else {
            code2 = link2;
        }

        if (code1 == null || code2 == null) {
            throw new NullPointerException("code1 and code2 is null");
        }

        List<String> blocks1 = code1.length() > 5000 ? splitIntoBlocks(code1, 5000) : List.of(code1);
        List<String> blocks2 = code2.length() > 5000 ? splitIntoBlocks(code2, 5000) : List.of(code2);

        double totalSimilarity = 0;
        int comparisons = 0;

        int maxBlocks = Math.max(blocks1.size(), blocks2.size());
        for (int i = 0; i < maxBlocks; i++) {
            String part1 = i < blocks1.size() ? blocks1.get(i) : ""; // Если закончились блоки 1, используем пустую строку
            String part2 = i < blocks2.size() ? blocks2.get(i) : ""; // Если закончились блоки 2, используем пустую строку

            // Считаем сходство текущих блоков
            double shingleSimilarity = shnigle.calculateShingleSimilarity(part1, part2, 6);
            double levenshtein = levenshteinSimilarity.calculateLevenshteinSimilarity(part1, part2);

            // Учитываем среднее сходство для блока
            totalSimilarity += (shingleSimilarity + levenshtein) / 2;
            comparisons++;
        }

        // Возвращаем общее сходство
        return comparisons > 0 ? totalSimilarity / comparisons : 0;
    }



    private List<String> splitIntoBlocks(String content, int blockSize) {
        List<String> blocks = new ArrayList<>();
        int length = content.length();

        for (int i = 0; i < length; i += blockSize) {
            blocks.add(content.substring(i, Math.min(length, i + blockSize)));
        }

        return blocks;
    }
}
