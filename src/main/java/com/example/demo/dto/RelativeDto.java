package com.example.demo.dto;

import com.example.demo.model.Relative;
import jakarta.validation.constraints.Size;

public record RelativeDto(long id,
                          @Size(min = 1, max = 15, message = "name`s length must be between 1 and 15") String name,
                          @Size(min = 1, max = 15, message = "phone`s length must be between 1 and 15") String phone,
                          @Size(min = 1, max = 15, message = "address`s length must be between 1 and 15") String address) {

    public RelativeDto(Relative relative) {
        this(relative.getId(), relative.getName(), relative.getPhone(), relative.getAddress());
    }
}
