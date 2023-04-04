package com.example.demo.dto;

import com.example.demo.model.Child;
import com.example.demo.model.Relative;

import java.util.List;

public record RelativeWithKidsAndGroups(String name, String phone, String address, List<String> childNames,
                                        List<String> groupName) {
    public RelativeWithKidsAndGroups(Relative relative) {
        this(relative.getName(), relative.getPhone(), relative.getAddress(),
                relative.getKids().stream().map(Child::getName).toList(),
                relative.getKids().stream().map(child -> child.getGroup().getName()).toList());
    }
}
