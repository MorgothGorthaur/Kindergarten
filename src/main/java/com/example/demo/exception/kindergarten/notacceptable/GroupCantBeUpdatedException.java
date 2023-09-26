package com.example.demo.exception.kindergarten.notacceptable;

public class GroupCantBeUpdatedException extends NotAcceptableDataException {
    public GroupCantBeUpdatedException(int maxSize) {
        super("Too many children in the group. You can`t change the maxSize to " + maxSize);
    }
}
