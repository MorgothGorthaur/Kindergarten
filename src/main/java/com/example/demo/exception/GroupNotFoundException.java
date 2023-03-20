package com.example.demo.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String email) {
        super("teacher with email " + email + " does`t have any group");
    }
}
