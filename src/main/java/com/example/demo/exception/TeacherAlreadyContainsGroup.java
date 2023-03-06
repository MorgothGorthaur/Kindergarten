package com.example.demo.exception;

public class TeacherAlreadyContainsGroup extends RuntimeException {
    public TeacherAlreadyContainsGroup(String email) {
        super("teacher with email " + email + " already contains group");
    }
}
