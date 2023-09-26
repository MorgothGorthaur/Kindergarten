package com.example.demo.exception.security;


public class BadPasswordOrEmailException extends SecurityAuthException {
    public BadPasswordOrEmailException(String email) {
        super("bad password and/or email! " + email);
    }
}
