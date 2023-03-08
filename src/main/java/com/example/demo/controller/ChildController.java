package com.example.demo.controller;

import com.example.demo.dto.ChildDto;
import com.example.demo.dto.ChildWithRelativeDto;
import com.example.demo.dto.Mapper;
import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/child")
@RequiredArgsConstructor
public class ChildController {
    private final ChildRepository repository;
    private final Mapper mapper;

    private final GroupRepository groupRepository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ChildDto> getAll(Principal principal) {
        return repository.getChildByTeacherEmail(principal.getName())
                .stream().map(mapper::toChildDto).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void add(Principal principal, @RequestBody ChildWithRelativeDto dto) {
        var group = groupRepository.getGroupByTeacherEmail(principal.getName())
                .orElseThrow(() -> new GroupNotFoundException(principal.getName()));
        group.addChild(dto.toChild());
        groupRepository.save(group);
    }
}
