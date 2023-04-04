package com.example.demo.dto;

import com.example.demo.model.Relative;

import java.util.List;

public record RelativeWithKidsAndGroups(String name, String phone, String address,
                                        List<ChildWithGroupDto> childWithGroup) {
    public RelativeWithKidsAndGroups(Relative relative) {
        this(relative.getName(), relative.getPhone(), relative.getAddress(),
                relative.getKids().stream().map(ChildWithGroupDto::new).toList());

    }
}
