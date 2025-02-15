package com.plagiarism.AntiPlagiarismBack.dto;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class HomeWorkContent {
    private String title;
    private List<UserContent> users;



}
