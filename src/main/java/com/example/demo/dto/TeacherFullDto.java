package com.example.demo.dto;

import com.example.demo.model.Teacher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TeacherFullDto(@Size(min = 1, max = 15, message = "name must be set") String name,
                             @Size(min = 1, max = 15, message = "phone must be set") String phone,
                             @Size(min = 1, max = 15, message = "skype must be set") String skype,
                             @NotNull(message = "email name mst be set!") @Email String email,
                             @NotNull(message = "password must be set!") String password) {
    public Teacher createTeacher() {
        return new Teacher(name, phone, skype, email, password);
    }
}
