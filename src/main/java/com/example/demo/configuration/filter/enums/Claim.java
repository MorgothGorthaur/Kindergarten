package com.example.demo.configuration.filter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Claim {
    ROLES("roles");
    private final String claim;
}
