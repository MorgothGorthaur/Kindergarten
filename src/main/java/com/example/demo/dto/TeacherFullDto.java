package com.example.demo.dto;

import com.example.demo.model.Teacher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TeacherFullDto(@Size(min = 1, max = 15, message = "name must be setted") String name,
                             @Size(min = 1, max = 15, message = "phone must be setted") String phone,
                             @Size(min = 1, max = 15, message = "skype must be setted") String skype,
                             @NotNull(message = "email name mst be setted!") @Email String email,
                             @NotNull(message = "password must be setted!") String password) {
    public Teacher toTeacher() {
        return new Teacher(name, phone, skype, email, password);
    }
}
