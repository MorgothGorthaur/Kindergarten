package com.example.demo.dto;

import com.example.demo.model.Teacher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record TeacherFullDto(@Size(min = 1, max = 35, message = "name`s length must be between 1 and 35") String name,
                             @Size(min = 1, max = 50, message = "phone`s length must be between 1 and 50") String phone,
                             @Size(min = 1, max = 50, message = "skype`s length must be between 1 and 50") String skype,
                             @Size(min = 1, max = 50, message = "email`s length must be between 1 and 50") @Email String email,
                             @Size(min = 5, max = 15, message = "password`s length must be between 5 and 15") String password) {
    public Teacher createTeacher() {
        return new Teacher(name, phone, skype, email, password);
    }
}
