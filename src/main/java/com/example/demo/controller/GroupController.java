package com.example.demo.controller;

import com.example.demo.dto.GroupDto;
import com.example.demo.dto.GroupWithTeacherDto;
import com.example.demo.dto.Mapper;
import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.exception.TeacherAlreadyContainsGroup;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.Actuality;
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
        return repository.getGroupsWithTeacher().stream().map(mapper::toGroupWithTeacherDto).toList();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public GroupDto getAll(Principal principal) {
        return repository.getGroupByTeacherEmailAndTeacherActuality(principal.getName(), Actuality.ACTIVE).map(mapper::toGroupDto).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void addGroup(Principal principal, @RequestBody GroupDto dto) {
        var teacher = teacherRepository.findTeacherByEmailAndActuality(principal.getName(), Actuality.ACTIVE)
                        .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.addGroup(dto.toGroup());
        teacherRepository.save(teacher);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateGroup(Principal principal, @RequestBody GroupDto dto) {
        var teacher = teacherRepository.findTeacherByEmailAndActuality(principal.getName(), Actuality.ACTIVE)
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        var group = teacher.getGroup();
        if(group != null) {
            group.setName(dto.name());
            group.setMaxSize(dto.maxSize());
            teacherRepository.save(teacher);
        } else throw new GroupNotFoundException(teacher.getEmail());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteGroup(Principal principal) {
        var teacher = teacherRepository.findTeacherByEmailAndActuality(principal.getName(), Actuality.ACTIVE)
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.removeGroup();
        teacherRepository.save(teacher);
    }
}
