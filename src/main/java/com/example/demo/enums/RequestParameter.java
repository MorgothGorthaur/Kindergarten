package com.example.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum RequestParameter {
    EMAIL("email"),
    PASSWORD("password");
    private final String parameter;
}