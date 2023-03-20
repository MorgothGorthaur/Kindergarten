package com.example.demo.exception;

public class GroupContainsKidsException extends RuntimeException {
    public GroupContainsKidsException() {
        super("this group contains kids!");
    }
}
