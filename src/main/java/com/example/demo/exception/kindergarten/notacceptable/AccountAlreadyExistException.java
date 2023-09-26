package com.example.demo.exception.kindergarten.notacceptable;

public class AccountAlreadyExistException extends NotAcceptableDataException {
    public AccountAlreadyExistException(String email) {
        super("account with email = " + email + " already exists");
    }
}
