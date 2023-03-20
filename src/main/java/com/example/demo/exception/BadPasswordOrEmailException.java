package com.example.demo.exception;

public class BadPasswordOrEmailException extends RuntimeException {
    public BadPasswordOrEmailException(String email, String password) {
        super("bad password and/or email! " + email + " " + password);
    }
}
