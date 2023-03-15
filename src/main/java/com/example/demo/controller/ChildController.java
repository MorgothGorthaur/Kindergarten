package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.model.Group;
import com.example.demo.model.Teacher;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.TeacherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/child")
@PreAuthorize("hasRole('ROLE_USER')")
@RequiredArgsConstructor
public class ChildController {
    private final ChildRepository repository;
    private final Mapper mapper;
    private final TeacherRepository teacherRepository;

    @GetMapping
    public List<ChildDto> getAll(Principal principal) {
        return repository.findKidsByTeacherEmail(principal.getName()).stream().map(mapper::toChildDto).toList();
    }

    @GetMapping("/full")
    public List<ChildFullDto> getFull(Principal principal) {
        return repository.findKidsWithRelativesByTeacherEmail(principal.getName())
                .stream().map(mapper::toChildFullDto).toList();
    }

    @GetMapping("/birth")
    public List<ChildDto> getChildThatWaitsBirth(Principal principal) {
        return repository.findKidsThatWaitBirthDay(principal.getName())
                .stream().map(mapper::toChildDto).toList();
    }

    @GetMapping("/{id}")
    public List<ChildWithGroupDto> getBrothersAndSisters(@PathVariable long id) {
        return repository.findBrothersAndSisters(id).stream().map(mapper::toChildWithGroupDto).toList();
    }

    @PostMapping
    public ChildDto add(Principal principal, @RequestBody @Valid ChildDto dto) {
        var group = teacherRepository.findTeacherWithGroupAndKidsByEmail(principal.getName())
                .map(Teacher::getGroup)
                .orElseThrow(() -> new GroupNotFoundException(principal.getName()));
        var child = dto.toChild();
        group.addChild(child);
        return mapper.toChildDto(repository.save(child));
    }

    @PatchMapping
    public void update(Principal principal, @RequestBody @Valid ChildDto dto) {
        var child = repository.findChildByIdAndTeacherEmail(dto.id(), principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        child.setName(dto.name());
        child.setBirthYear(dto.birthYear());
        repository.save(child);

    }

    @DeleteMapping("/{id}")
    public void delete(Principal principal, @PathVariable long id) {
        var child = repository.findChildWithRelativesByIdAndTeacherEmail(id, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        child.setRelatives(null);
        repository.delete(child);
    }


}
