package com.example.demo.configuration.filter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorizationType {
    BEARER("Bearer ");
    private final String prefix;
}
