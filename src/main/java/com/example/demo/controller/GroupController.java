package com.example.demo.controller;

import com.example.demo.dto.GroupDto;
import com.example.demo.dto.GroupWithTeacherDto;
import com.example.demo.dto.Mapper;
import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupRepository repository;
    private final TeacherRepository teacherRepository;
    private final Mapper mapper;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<GroupWithTeacherDto> getAll() {
        return repository.findAll().stream().map(mapper::toGroupWithTeacherDto).toList();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public GroupDto getGroup(Principal principal) {
        return repository.getGroupByTeacherEmail(principal.getName()).map(mapper::toGroupDto).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void addGroup(Principal principal, @RequestBody GroupDto dto) {
        var teacher = teacherRepository.findTeacherByEmail(principal.getName())
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.addGroup(dto.toGroup());
        repository.save(teacher.getGroup());
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateGroup(Principal principal, @RequestBody GroupDto dto) {
        var group = repository.getGroupByTeacherEmail(principal.getName())
                .orElseThrow(() -> new GroupNotFoundException(principal.getName()));
        group.setName(dto.name());
        group.setMaxSize(dto.maxSize());
        repository.save(group);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteGroup(Principal principal) {
        var group = repository.getGroupWithKidsByTeacherEmail(principal.getName())
                .orElseThrow(() -> new GroupNotFoundException(principal.getName()));
        group.getTeacher().removeGroup();
        repository.delete(group);
    }
}
