package com.example.demo.dto;

import com.example.demo.model.Child;
import com.example.demo.validation.DateValidation;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@DateValidation
public record ChildDto(long id,
                       @Size(min = 1, max = 35, message = "name`s length must be between 1 and 35") String name,
                       LocalDate birthYear) {
    public Child createChild() {
        return new Child(name, birthYear);
    }

    public ChildDto(Child child) {
        this(child.getId(), child.getName(), child.getBirthYear());
    }
}
