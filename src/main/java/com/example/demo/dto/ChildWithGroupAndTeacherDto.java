package com.example.demo.dto;

import com.example.demo.model.Child;

import java.time.LocalDate;

public record ChildWithGroupAndTeacherDto(long id, String name, LocalDate birthYear, String groupName,
                                          String teacherEmail) {
    public ChildWithGroupAndTeacherDto(Child child) {
        this(child.getId(), child.getName(), child.getBirthYear(), child.getGroup().getName(), child.getGroup().getTeacher().getEmail());
    }
}
