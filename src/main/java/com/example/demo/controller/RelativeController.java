package com.example.demo.controller;

import com.example.demo.dto.RelativeDto;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.exception.RelativeNotFoundException;
import com.example.demo.model.Relative;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.RelativeRepository;
import com.example.demo.service.RelativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/relative")
@PreAuthorize("hasRole('ROLE_USER')")
@RequiredArgsConstructor
public class RelativeController {
    private final RelativeRepository repository;
    private final RelativeService service;

    @GetMapping("/{childId}")
    public List<RelativeDto> getAll(Principal principal, @PathVariable long childId) {
        return repository.findRelativesByChildIdAndTeacherEmail(childId, principal.getName())
                .stream().map(RelativeDto::new).toList();
    }

    @PostMapping("/{childId}")
    public RelativeDto add(Principal principal, @PathVariable long childId, @RequestBody RelativeDto dto) {
        return new RelativeDto(service.add(principal.getName(), childId, dto.toRelative()));
    }

    @DeleteMapping("/{childId}/{relativeId}")
    public void delete(Principal principal, @PathVariable long childId, @PathVariable long relativeId) {
        service.delete(principal.getName(), childId, relativeId);
    }


    @PatchMapping("/{childId}")
    public void update(Principal principal, @PathVariable long childId, @RequestBody RelativeDto dto) {
        service.updateOrReplaceRelative(principal.getName(), childId, dto.id(), dto.toRelative());
    }
}
