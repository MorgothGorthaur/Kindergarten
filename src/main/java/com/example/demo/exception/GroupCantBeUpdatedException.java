package com.example.demo.exception;

public class GroupCantBeUpdatedException extends RuntimeException {
    public GroupCantBeUpdatedException(int maxSize) {
        super("Too many children in the group. You can`t change the maxSize to " + maxSize);
    }
}
