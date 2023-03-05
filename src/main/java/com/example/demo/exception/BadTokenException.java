package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BadTokenException extends RuntimeException {
    public BadTokenException(String message) {
        super(message);
        log.error("error logging in: {} ", message);
    }
}
