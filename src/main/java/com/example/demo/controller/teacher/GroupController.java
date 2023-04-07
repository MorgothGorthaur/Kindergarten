package com.example.demo.controller.teacher;

import com.example.demo.dto.GroupDto;
import com.example.demo.dto.GroupWithCurrentSizeDto;
import com.example.demo.repository.GroupRepository;
import com.example.demo.service.GroupService;
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
    private final GroupService service;

    @GetMapping
    public GroupWithCurrentSizeDto getGroup(Principal principal) {
        return repository.findGroupByTeacherEmail(principal.getName())
                .map(GroupWithCurrentSizeDto::new).orElse(null);
    }

    @PostMapping
    public void add(Principal principal, @RequestBody @Valid GroupDto dto) {
        service.save(principal.getName(), dto.createGroup());
    }

    @PatchMapping
    public void update(Principal principal, @RequestBody @Valid GroupDto dto) {
        service.update(principal.getName(), dto.name(), dto.maxSize());
    }

    @DeleteMapping
    public void remove(Principal principal) {
        service.delete(principal.getName());
    }
}
