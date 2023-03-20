package com.example.demo.dto;

import com.example.demo.model.Teacher;

public record TeacherDto(String name, String phone, String skype, String email) {
    public TeacherDto(Teacher teacher) {
        this(teacher.getName(), teacher.getPhone(), teacher.getSkype(), teacher.getEmail());
    }
}
