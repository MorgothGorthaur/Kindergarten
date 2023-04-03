package com.example.demo.dto;

import com.example.demo.model.Admin;

public record AdminFullDto(String email, String password, String phone) {

    public Admin createAdmin() {
        return new Admin(email, password, phone);
    }
}
