package com.example.demo.exception.kindergarten.notfound;

public class TeacherNotFoundException extends EntityNotFoundException {
    public TeacherNotFoundException(String email) {
        super("teacher with email = " + email + " not founded!");
    }
}
