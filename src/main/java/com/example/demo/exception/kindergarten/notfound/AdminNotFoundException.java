package com.example.demo.exception.kindergarten.notfound;

public class AdminNotFoundException extends EntityNotFoundException {
    public AdminNotFoundException(String email) {
        super("admin with email = " + email + " not founded!");
    }
}
