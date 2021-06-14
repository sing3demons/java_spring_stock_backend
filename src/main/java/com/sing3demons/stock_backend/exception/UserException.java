package com.sing3demons.stock_backend.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super("Email: " + message + " already exists.");
    }
}
