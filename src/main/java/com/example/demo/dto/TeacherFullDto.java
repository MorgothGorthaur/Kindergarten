package com.example.demo.dto;

import com.example.demo.model.Teacher;

public record TeacherFullDto(String name, String phone, String skype, String email, String password) {
    public Teacher toTeacher() {
        return new Teacher(name, phone, skype, email, password);
    }
}
