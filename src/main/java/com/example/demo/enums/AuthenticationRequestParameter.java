package com.example.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum AuthenticationRequestParameter {
    EMAIL("email"),
    PASSWORD("password");
    private final String parameter;
}