package com.example.demo.exception;

public class TeacherAlreadyContainsGroupException extends RuntimeException {
    public TeacherAlreadyContainsGroupException(String email) {
        super("teacher with email " + email + " already contains group");
    }
}
