package com.example.demo.dto;

import com.example.demo.model.Group;

public record GroupDto(String name, int maxSize) {
    public Group toGroup() {
        return new Group(name, maxSize);
    }
}
