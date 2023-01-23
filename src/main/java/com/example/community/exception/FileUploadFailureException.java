package com.example.community.exception;

public class FileUploadFailureException extends RuntimeException{
    public FileUploadFailureException(Throwable cause) {
        super(cause);
    }
}
