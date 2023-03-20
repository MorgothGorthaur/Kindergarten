package com.example.demo.exception;

public class ChildNotFoundException extends RuntimeException {
    public ChildNotFoundException(String email) {
        super("teacher with email " + email + " does`t contains this child");
    }
}
