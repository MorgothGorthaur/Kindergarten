package com.example.demo.controller.admin;

import com.example.demo.dto.ChildWithGroupAndTeacherDto;
import com.example.demo.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kindergarten/admin/kids")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AllKidsController {
    private final ChildRepository repository;

    @GetMapping
    public List<ChildWithGroupAndTeacherDto> get() {
        return repository.findAllWithGroupsAndTeachers().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @GetMapping("/group_name")
    public List<ChildWithGroupAndTeacherDto> getByGroupName() {
        return repository.findAllWithGroupsAndTeachersSortedByGroupName().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @GetMapping("/teacher_email")
    public List<ChildWithGroupAndTeacherDto> getByTeacherName() {
        return repository.findAllWithGroupsAndTeachersSortedByTeacherEmail().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @GetMapping("/name")
    public List<ChildWithGroupAndTeacherDto> getByName() {
        return repository.findAllWithGroupsAndTeachersSortedByName().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }


    @GetMapping("/birth")
    public List<ChildWithGroupAndTeacherDto> getByBirth() {
        return repository.findAllWithGroupsAndTeachersSortedByBirth().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

}
