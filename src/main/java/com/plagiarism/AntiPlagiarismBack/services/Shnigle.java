package com.plagiarism.AntiPlagiarismBack.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class Shnigle {
    public double calculateShingleSimilarity(String str1, String str2, int shingleSize) {
        Set<String> shingles1 = generateShingles(str1, shingleSize);
        Set<String> shingles2 = generateShingles(str2, shingleSize);

        Set<String> intersection = new HashSet<>(shingles1);
        intersection.retainAll(shingles2);

        Set<String> union = new HashSet<>(shingles1);
        union.addAll(shingles2);

        if (union.isEmpty()) return 100.0;

        return ((double) intersection.size() / union.size()) * 100;
    }

    private Set<String> generateShingles(String text, int shingleSize) {
        Set<String> shingles = new HashSet<>();
        for (int i = 0; i <= text.length() - shingleSize; i++) {
            shingles.add(text.substring(i, i + shingleSize));
        }
        return shingles;
    }
}
