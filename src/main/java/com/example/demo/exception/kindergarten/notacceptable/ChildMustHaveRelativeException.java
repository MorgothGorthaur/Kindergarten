package com.example.demo.exception.kindergarten.notacceptable;

public class ChildMustHaveRelativeException extends NotAcceptableDataException {
    public ChildMustHaveRelativeException() {
        super("child must have a relative!");
    }
}
