package com.example.demo.exception.kindergarten.notacceptable;

public class TeacherAlreadyContainsGroupException extends NotAcceptableDataException {
    public TeacherAlreadyContainsGroupException(String email) {
        super("teacher with email " + email + " already contains group");
    }
}
