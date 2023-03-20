package com.example.demo.exception;

public class ToBigChildrenInGroupException extends RuntimeException {
    public ToBigChildrenInGroupException(int maxSize) {
        super("max size of kids in this group is " + maxSize);
    }
}
