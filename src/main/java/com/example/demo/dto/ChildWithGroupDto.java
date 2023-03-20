package com.example.demo.dto;

import com.example.demo.model.Child;

import java.time.LocalDate;

public record ChildWithGroupDto(String name, LocalDate birthYear, String groupName) {
    public ChildWithGroupDto(Child child) {
        this(child.getName(), child.getBirthYear(), child.getGroup().getName());
    }
}
