package com.example.demo.controller;

import com.example.demo.dto.GroupDto;
import com.example.demo.dto.GroupWithCurrentSizeDto;
import com.example.demo.dto.Mapper;
import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.exception.ToBigChildrenInGroupException;
import com.example.demo.model.Teacher;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.TeacherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/kindergarten/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupRepository repository;
    private final TeacherRepository teacherRepository;
    private final Mapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public GroupWithCurrentSizeDto getGroup(Principal principal) {
        return teacherRepository.findTeacherWithGroupAndKidsByEmail(principal.getName()).map(Teacher::getGroup).map(mapper::toGroupWithCurrentSizeDto).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void add(Principal principal, @RequestBody @Valid GroupDto dto) {
        var teacher = teacherRepository.findTeacherByEmail(principal.getName())
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.addGroup(dto.toGroup());
        repository.save(teacher.getGroup());
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(Principal principal, @RequestBody @Valid GroupDto dto) {
        var group = teacherRepository.findTeacherWithGroupAndKidsByEmail(principal.getName())
                .map(Teacher::getGroup)
                .orElseThrow(() -> new GroupNotFoundException(principal.getName()));
        if(!group.isAbleToBeUpdated(dto.maxSize())) throw new ToBigChildrenInGroupException(dto.maxSize());
        group.setName(dto.name());
        group.setMaxSize(dto.maxSize());
        repository.save(group);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void remove(Principal principal) {
        var group = teacherRepository.findTeacherWithGroupAndKidsByEmail(principal.getName())
                .map(Teacher::getGroup)
                .orElseThrow(() -> new GroupNotFoundException(principal.getName()));
        group.getTeacher().removeGroup();
        repository.delete(group);
    }
}
