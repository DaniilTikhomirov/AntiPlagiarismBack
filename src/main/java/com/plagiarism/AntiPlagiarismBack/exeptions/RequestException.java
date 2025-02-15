package com.plagiarism.AntiPlagiarismBack.exeptions;

public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }
}
