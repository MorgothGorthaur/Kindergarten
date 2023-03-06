package com.example.demo.dto;

import com.example.demo.model.Group;

public record GroupDto(String name, int maxSize, int currentSize) {
    public Group toGroup() {
        return new Group(name, maxSize);
    }
}
