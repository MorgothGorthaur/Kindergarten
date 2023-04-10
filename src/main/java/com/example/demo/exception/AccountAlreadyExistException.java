package com.example.demo.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String email) {
        super("account with email = " + email + " already exists");
    }
}
