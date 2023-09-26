package com.example.demo.exception.kindergarten.notfound;

import com.example.demo.exception.kindergarten.KindergartenException;


public class EntityNotFoundException extends KindergartenException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
