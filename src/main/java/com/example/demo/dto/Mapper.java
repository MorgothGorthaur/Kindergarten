package com.example.demo.dto;

import com.example.demo.model.Child;
import com.example.demo.model.Group;
import com.example.demo.model.Relative;
import com.example.demo.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public TeacherDto toTeacherDto(Teacher teacher) {
        return new TeacherDto(teacher.getName(), teacher.getPhone(), teacher.getSkype());
    }

    public TeacherWithGroupDto toTeacherWithGroupDto(Teacher teacher) {
        var group = teacher.getGroup();
        return new TeacherWithGroupDto(teacher.getName(), teacher.getPhone(), teacher.getSkype(), group != null ? group.getName() : "without group");
    }

    public GroupWithTeacherDto toGroupWithTeacherDto(Group group) {
        return new GroupWithTeacherDto(group.getName(), group.getMaxSize(),toTeacherDto(group.getTeacher()));
    }

    public GroupDto toGroupDto(Group group) {
        return new GroupDto(group.getName(), group.getMaxSize());
    }

    public ChildDto toChildDto(Child child) {
        return new ChildDto(child.getId(), child.getName(), child.getBirthYear());
    }

    public ChildFullDto toChildFullDto(Child child) {
        return new ChildFullDto(child.getName(), child.getBirthYear(), child.getRelatives().stream().map(this::toRelativeDto).toList());
    }

    public RelativeDto toRelativeDto(Relative relative) {
        return new RelativeDto(relative.getId(), relative.getName(), relative.getPhone(), relative.getAddress());
    }

    public ChildWithGroupDto toChildWithGroupDto(Child child) {
        return new ChildWithGroupDto(child.getName(), child.getBirthYear(), child.getGroup().getName());
    }
}
