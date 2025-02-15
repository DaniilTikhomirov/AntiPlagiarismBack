package com.plagiarism.AntiPlagiarismBack.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class StudentDto {
    private String firstName;
    private String lastName;


    @Override
    public String toString() {
        return "{" +
                "\"firstName\": " +  "\"" + firstName + "\"" + "," +
                "\"lastName\": " +  "\"" + lastName + "\"" +
                '}';
    }
}
