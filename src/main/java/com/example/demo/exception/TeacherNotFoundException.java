package com.example.demo.exception;

public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(String email) {
        super("teacher with email = " + email + " not founded!");
    }
}
