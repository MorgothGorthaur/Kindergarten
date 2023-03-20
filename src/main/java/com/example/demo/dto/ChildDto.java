package com.example.demo.dto;

import com.example.demo.model.Child;
import com.example.demo.validation.DateValidation;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@DateValidation
public record ChildDto(long id,
                       @Size(min = 1, max = 15, message = "name must be set") String name,
                       LocalDate birthYear) {
    public Child toChild() {
        return new Child(name, birthYear);
    }

    public ChildDto(Child child) {
        this(child.getId(), child.getName(), child.getBirthYear());
    }
}
