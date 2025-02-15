package com.plagiarism.AntiPlagiarismBack.services;


import org.springframework.stereotype.Service;

@Service
public class LevenshteinSimilarity {

    public double calculateLevenshteinSimilarity(String str1, String str2) {
        int distance = levenshteinDistance(str1, str2);
        int maxLength = Math.max(str1.length(), str2.length());

        // Если обе строки пустые, считаем их 100% одинаковыми
        if (maxLength == 0) return 100.0;

        // Считаем процент схожести
        return (1 - (double) distance / maxLength) * 100;
    }

    public int levenshteinDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();

        // Создаем матрицу для хранения расстояний
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;

        // Заполнение матрицы
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),  // Удаление или вставка
                        dp[i - 1][j - 1] + cost                       // Замена
                );
            }
        }

        return dp[len1][len2];
    }

}
