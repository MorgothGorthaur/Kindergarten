package com.example.demo.controller;

import com.example.demo.dto.ChildDto;
import com.example.demo.dto.ChildFullDto;
import com.example.demo.dto.ChildWithGroupDto;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.repository.ChildRepository;
import com.example.demo.service.ChildService;
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
    private final ChildService service;

    @GetMapping
    public List<ChildDto> getAll(Principal principal) {
        return repository.findKidsByTeacherEmail(principal.getName()).stream().map(ChildDto::new).toList();
    }

    @GetMapping("/full")
    public List<ChildFullDto> getFull(Principal principal) {
        return repository.findKidsWithRelativesByTeacherEmail(principal.getName())
                .stream().map(ChildFullDto::new).toList();
    }

    @GetMapping("/birth")
    public List<ChildDto> getChildThatWaitsBirth(Principal principal) {
        return repository.findKidsThatWaitBirthDay(principal.getName())
                .stream().map(ChildDto::new).toList();
    }

    @GetMapping("/{id}")
    public List<ChildWithGroupDto> getBrothersAndSisters(@PathVariable long id) {
        return repository.findBrothersAndSisters(id).stream().map(ChildWithGroupDto::new).toList();
    }

    @PostMapping
    public ChildDto add(Principal principal, @RequestBody @Valid ChildDto dto) {
        return new ChildDto(service.add(principal.getName(), dto.toChild()));
    }

    @PatchMapping
    public void update(Principal principal, @RequestBody @Valid ChildDto dto) {
        if (repository.updateChild(principal.getName(), dto.id(), dto.name(), dto.birthYear()) == 0)
            throw new ChildNotFoundException(principal.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(Principal principal, @PathVariable long id) {
        if (repository.deleteChild(principal.getName(), id) == 0) throw new ChildNotFoundException(principal.getName());
    }

}
