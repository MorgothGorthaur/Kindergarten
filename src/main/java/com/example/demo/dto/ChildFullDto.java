package com.example.demo.dto;

import com.example.demo.model.Child;

import java.time.LocalDate;
import java.util.List;


public record ChildFullDto(String name, LocalDate birthYear, List<RelativeDto> relatives) {
    public ChildFullDto(Child child) {
        this(child.getName(), child.getBirthYear(), child.getRelatives().stream().map(RelativeDto::new).toList());
    }
}
