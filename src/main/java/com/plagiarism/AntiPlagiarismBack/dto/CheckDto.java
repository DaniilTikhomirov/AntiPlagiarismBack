package com.plagiarism.AntiPlagiarismBack.dto;

import lombok.Data;

@Data
public class CheckDto {
    private StudentDto student1;
    private StudentDto student2;
    private double similarity;
}
