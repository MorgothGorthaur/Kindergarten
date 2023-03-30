package com.example.demo.exception;

public class TeacherAlreadyExistException extends RuntimeException {
    public TeacherAlreadyExistException(String email) {
        super("teacher with email = " + email + " already exists");
    }
}
