package com.example.demo.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String email) {
        super("admin with email = " + email + " not founded!");
    }
}
