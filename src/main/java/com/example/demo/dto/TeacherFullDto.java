package com.example.demo.dto;

import com.example.demo.model.Actuality;
import com.example.demo.model.Role;
import com.example.demo.model.Teacher;

public record TeacherFullDto(String name, String phone, String skype, String email, String password) {
    public Teacher toTeacher() {
        var teacher = new Teacher();
        teacher.setName(name);
        teacher.setPhone(phone);
        teacher.setSkype(skype);
        teacher.setEmail(email);
        teacher.setPassword(password);
        teacher.setActuality(Actuality.ACTIVE);
        teacher.setRole(Role.ROLE_USER);
        return teacher;
    }
}
