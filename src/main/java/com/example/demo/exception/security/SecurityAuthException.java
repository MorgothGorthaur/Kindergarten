package com.example.demo.exception.security;

public class SecurityAuthException extends RuntimeException {
    public SecurityAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityAuthException(String message) {
        super(message);
    }
}
