package com.example.demo.dto;

import com.example.demo.model.Group;

public record GroupWithCurrentSizeDto(String name, int maxSize, int currentSize) {
    public GroupWithCurrentSizeDto(Group group) {
        this(group.getName(), group.getMaxSize(), group.getCurrentSize());
    }
}
