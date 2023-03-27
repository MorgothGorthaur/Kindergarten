package com.example.demo.exception;

public class BadPasswordOrEmailException extends RuntimeException {
    public BadPasswordOrEmailException(String email) {
        super("bad password and/or email! " + email);
    }
}
