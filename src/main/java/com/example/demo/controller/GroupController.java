package com.example.demo.controller;

import com.example.demo.dto.GroupDto;
import com.example.demo.dto.GroupWithCurrentSizeDto;
import com.example.demo.dto.Mapper;
import com.example.demo.exception.*;
import com.example.demo.model.Teacher;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/kindergarten/group")
@PreAuthorize("hasRole('ROLE_USER')")
@RequiredArgsConstructor
public class GroupController {
    private final GroupRepository repository;
    private final TeacherRepository teacherRepository;
    private final Mapper mapper;

    @GetMapping
    public GroupWithCurrentSizeDto getGroup(Principal principal) {
        return teacherRepository.findTeacherWithGroupAndKidsByEmail(principal.getName()).map(Teacher::getGroup).map(mapper::toGroupWithCurrentSizeDto).orElse(null);
    }

    @PostMapping
    public void add(Principal principal, @RequestBody @Valid GroupDto dto) {
        var teacher = teacherRepository.findTeacherByEmail(principal.getName())
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.addGroup(dto.toGroup());
        repository.save(teacher.getGroup());
    }

    @PatchMapping
    @Transactional
    public void update(Principal principal, @RequestBody @Valid GroupDto dto) {
        if(repository.updateGroup(principal.getName(), dto.name(), dto.maxSize()) == 0) throw new ToBigChildrenInGroupException(dto.maxSize());
    }

    @DeleteMapping
    @Transactional
    public void remove(Principal principal) {
        if(repository.deleteGroupFromTeacher(principal.getName()) == 0) throw new GroupContainsKidsException();
    }
}
