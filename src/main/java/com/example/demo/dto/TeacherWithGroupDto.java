package com.example.demo.dto;

import com.example.demo.model.Teacher;

public record TeacherWithGroupDto(String name, String phone, String skype, String groupName) {
    public TeacherWithGroupDto(Teacher teacher) {
        this(teacher.getName(), teacher.getPhone(), teacher.getSkype(), teacher.getGroup() != null ? teacher.getGroup().getName() : "without group");
    }
}
