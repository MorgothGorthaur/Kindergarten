package com.example.demo.exception.security;


public class BadTokenException extends SecurityAuthException {
    public BadTokenException() {
        super("your token is missed or outdated, you must login to continue");
    }
}
