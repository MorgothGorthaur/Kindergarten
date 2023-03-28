package com.example.demo.dto;

import com.example.demo.model.Relative;
import jakarta.validation.constraints.Size;

public record RelativeDto(long id,
                          @Size(min = 1, max = 35, message = "name`s length must be between 1 and 35") String name,
                          @Size(min = 1, max = 50, message = "phone`s length must be between 1 and 50") String phone,
                          @Size(min = 1, max = 50, message = "address`s length must be between 1 and 50") String address) {

    public RelativeDto(Relative relative) {
        this(relative.getId(), relative.getName(), relative.getPhone(), relative.getAddress());
    }
}
