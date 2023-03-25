package com.example.demo.exception;

public class TooManyChildrenInGroupException extends RuntimeException {
    public TooManyChildrenInGroupException(int maxSize) {
        super("max size of kids in this group is " + maxSize);
    }
}
