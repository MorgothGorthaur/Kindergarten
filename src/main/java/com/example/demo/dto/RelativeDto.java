package com.example.demo.dto;

import com.example.demo.model.Relative;

public record RelativeDto(long id, String name, String phone, String address) {
    public Relative toRelative() {
        return new Relative(name, phone, address);
    }
}