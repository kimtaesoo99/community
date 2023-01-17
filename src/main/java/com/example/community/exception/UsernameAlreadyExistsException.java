package com.example.community.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
