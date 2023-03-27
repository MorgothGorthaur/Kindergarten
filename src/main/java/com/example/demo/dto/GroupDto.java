package com.example.demo.dto;

import com.example.demo.model.Group;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record GroupDto(@Size(min = 1, max = 15, message = "name`s length must be between 1 and 15") String name,
                       @Min(value = 1, message = "max size must be bigger then zero")
                       @Max(value = 99, message = "max size must be lover then hundred") int maxSize) {
    public Group createGroup() {
        return new Group(name, maxSize);
    }
}
