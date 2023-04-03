package com.example.demo.exception;

public class AdminAlreadyExistException extends RuntimeException {
    public AdminAlreadyExistException(String email) {
        super("admin with email = " + email + " already exists");
    }
}
