package com.example.demo.exception.kindergarten.notacceptable;

public class GroupContainsKidsException extends NotAcceptableDataException {
    public GroupContainsKidsException() {
        super("this group contains kids!");
    }
}
