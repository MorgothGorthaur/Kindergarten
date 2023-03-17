package com.example.demo.configuration.filter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Urls {
    LOGIN("${project.login.url}"),
    REFRESH("${project.refresh.url}");
    private final String url;
}
