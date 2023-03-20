package com.example.demo.dto;

import com.example.demo.model.Relative;
import jakarta.validation.constraints.Size;

public record RelativeDto(long id,
                          @Size(min = 1, max = 15, message = "name must be set") String name,
                          @Size(min = 1, max = 15, message = "phone must be set") String phone,
                          @Size(min = 1, max = 15, message = "skype must be set") String address) {

    public RelativeDto(Relative relative) {
        this(relative.getId(), relative.getName(), relative.getPhone(), relative.getAddress());
    }
}
