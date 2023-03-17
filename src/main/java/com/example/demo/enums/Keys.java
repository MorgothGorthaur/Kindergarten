package com.example.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Keys {
    SECRET_KEY("${jwt.secret.key}");
    private final String value;
}
