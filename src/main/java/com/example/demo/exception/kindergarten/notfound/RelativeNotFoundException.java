package com.example.demo.exception.kindergarten.notfound;

public class RelativeNotFoundException extends EntityNotFoundException {
    public RelativeNotFoundException() {
        super("relative not found");
    }
}
