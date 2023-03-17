package com.example.demo.configuration.filter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenTime {
    ACCESS_TOKEN("${jwt.access_token.time}"),
    REFRESH_TOKEN("${jwt.refresh_token.time}");
    private final String time;
    public long getTime() {
        return Long.parseLong(time);
    }
}
