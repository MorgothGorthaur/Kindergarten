package com.example.demo.exception;

public class TeacherAlreadyExist extends RuntimeException {
    public TeacherAlreadyExist(String email) {
        super("teacher with email = " + email + " already exists");
    }
}
