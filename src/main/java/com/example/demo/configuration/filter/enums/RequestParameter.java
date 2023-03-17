package com.example.demo.configuration.filter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum RequestParameter {
    EMAIL("email"),
    PASSWORD("password");
    private final String parameter;
}