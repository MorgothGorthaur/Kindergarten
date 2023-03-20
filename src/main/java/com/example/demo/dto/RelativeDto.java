package com.example.demo.dto;

import com.example.demo.model.Relative;
import jakarta.validation.constraints.Size;

public record RelativeDto(long id,
                          @Size(min = 1, max = 15, message = "name must be setted") String name,
                          @Size(min = 1, max = 15, message = "phone must be setted") String phone,
                          @Size(min = 1, max = 15, message = "skype must be setted") String address) {
    public Relative toRelative() {
        return new Relative(name, phone, address);
    }

    public RelativeDto(Relative relative) {
        this(relative.getId(), relative.getName(), relative.getPhone(), relative.getAddress());
    }
}
