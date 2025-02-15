package com.plagiarism.AntiPlagiarismBack.exeptions;

public class InvalidLinkException extends RuntimeException {
    public InvalidLinkException(String message) {
        super(message);
    }
}
