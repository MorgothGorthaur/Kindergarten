package com.example.demo.exception;

public class ToManyChildrenInGroupException extends RuntimeException {
    public ToManyChildrenInGroupException(int maxSize) {
        super("max size of kids in this group is " + maxSize);
    }
}
