package com.example.demo.exception;

public class RelativeNotFoundException extends RuntimeException {
    public RelativeNotFoundException() {
        super("relative not found exception");
    }
}
