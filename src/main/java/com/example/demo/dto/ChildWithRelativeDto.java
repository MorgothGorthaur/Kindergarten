package com.example.demo.dto;

import com.example.demo.exception.ChildMustHaveRelativeException;
import com.example.demo.model.Child;

import java.time.LocalDate;

public record ChildWithRelativeDto(String name, LocalDate birthYear, RelativeDto relative) {
    public ChildWithRelativeDto {
        if (relative == null) throw new ChildMustHaveRelativeException();
    }

    public Child toChild() {
        return new Child(name, birthYear, relative.toRelative());
    }
}
