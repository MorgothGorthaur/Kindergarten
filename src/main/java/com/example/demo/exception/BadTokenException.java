package com.example.demo.exception;


public class BadTokenException extends RuntimeException {
    public BadTokenException() {
        super("your token is missed or outdated, you must login to continue");
    }
}
