package com.example.demo.exception;

public class ChildMustHaveRelativeException extends RuntimeException {
    public ChildMustHaveRelativeException() {
        super("child must have a relative!");
    }
}
