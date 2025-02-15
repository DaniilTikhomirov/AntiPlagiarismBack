package com.plagiarism.AntiPlagiarismBack.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AddCheckWithStudent {
    private long student1_id;
    private long student2_id;
    private double similarity;

    @Override
    public String toString() {
        return "{" +
                "\"student1\":"  + student1_id  + "," +
                "\"student2\":"  + student2_id + "," +
                "\"similarity\":"  + similarity  +
                '}';
    }
}
