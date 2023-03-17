package com.example.demo.configuration.filter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Keys {
    SECRET_KEY("${jwt.secret.key}");
    private final String value;
}
