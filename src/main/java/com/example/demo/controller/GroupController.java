package com.example.demo.controller;

import com.example.demo.dto.GroupDto;
import com.example.demo.dto.GroupWithTeacherDto;
import com.example.demo.dto.Mapper;
import com.example.demo.model.Actuality;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/group")
@RequiredArgsConstructor
public class GroupController {
    private GroupRepository repository;
    private Mapper mapper;
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<GroupWithTeacherDto> getAll() {
        return repository.getGroupsWithTeacher().stream().map(mapper::toGroupWithTeacherDto).toList();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<GroupDto> getAll(Principal principal) {
        return repository.getGroupsByTeacherEmailAndTeacherActuality(principal.getName(), Actuality.ACTIVE)
                .stream().map(mapper::toGroupDto).toList();
    }
}
