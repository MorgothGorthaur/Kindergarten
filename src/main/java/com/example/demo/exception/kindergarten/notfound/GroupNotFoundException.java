package com.example.demo.exception.kindergarten.notfound;

public class GroupNotFoundException extends EntityNotFoundException {
    public GroupNotFoundException(String email) {
        super("teacher with email " + email + " does`t have any group");
    }
}
