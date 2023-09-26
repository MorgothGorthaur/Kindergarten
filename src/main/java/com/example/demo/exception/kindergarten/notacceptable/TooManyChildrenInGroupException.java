package com.example.demo.exception.kindergarten.notacceptable;

public class TooManyChildrenInGroupException extends NotAcceptableDataException {
    public TooManyChildrenInGroupException(int maxSize) {
        super("max size of kids in this group is " + maxSize);
    }
}
