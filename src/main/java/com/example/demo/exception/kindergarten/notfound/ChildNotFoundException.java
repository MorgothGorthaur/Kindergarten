package com.example.demo.exception.kindergarten.notfound;

public class ChildNotFoundException extends EntityNotFoundException {
    public ChildNotFoundException(String email) {
        super("teacher with email " + email + " does`t contains this child");
    }

    public ChildNotFoundException(long id) {
        super("child with id "  + id + " not found");
    }
}
