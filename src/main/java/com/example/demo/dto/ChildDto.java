package com.example.demo.dto;

import com.example.demo.model.Child;

import java.time.LocalDate;

public record ChildDto(String name, LocalDate birthYear) {
    public Child toChild() {
        return new Child(name, birthYear);
    }
}
