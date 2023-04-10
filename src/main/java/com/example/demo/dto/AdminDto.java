package com.example.demo.dto;

import com.example.demo.model.Admin;

public record AdminDto(String email, String phone) {
    public AdminDto(Admin admin) {
        this(admin.getEmail(), admin.getPhone());
    }
}
