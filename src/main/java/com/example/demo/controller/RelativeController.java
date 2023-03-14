package com.example.demo.controller;

import com.example.demo.dto.Mapper;
import com.example.demo.dto.RelativeDto;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.exception.RelativeNotFoundException;
import com.example.demo.model.Child;
import com.example.demo.model.Relative;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.RelativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/relative")
@RequiredArgsConstructor
public class RelativeController {
    private final RelativeRepository repository;
    private final ChildRepository childRepository;
    private final Mapper mapper;

    @GetMapping("/{childId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<RelativeDto> getAll(Principal principal, @PathVariable long childId) {
        return childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .stream().flatMap(child -> child.getRelatives().stream())
                .map(mapper::toRelativeDto).toList();
    }

    @PostMapping("/{childId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RelativeDto add(Principal principal, @PathVariable long childId, @RequestBody RelativeDto dto) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        var relative = repository.findEqualRelative(dto.name(), dto.address(), dto.phone()).orElseGet(dto::toRelative);
        child.addRelative(relative);
        return mapper.toRelativeDto(repository.save(relative));
    }

    @DeleteMapping("/{childId}/{relativeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void delete(Principal principal, @PathVariable long childId, @PathVariable long relativeId) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        var relative = repository.findById(relativeId)
                .orElseThrow(RelativeNotFoundException::new);
        child.removeRelative(relative);
        childRepository.save(child);
    }


    @PatchMapping("/{childId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(Principal principal, @PathVariable long childId, @RequestBody RelativeDto dto) {
        var relative = repository.findRelativeByRelativeIdAndChildIdAndTeacherEmail(dto.id(), childId, principal.getName())
                .orElseThrow(RelativeNotFoundException::new);
        repository.findEqualRelativeWithAnotherId(dto.name(), dto.address(), dto.phone(), dto.id())
                .ifPresentOrElse(equalRelative -> replaceRelative(principal, childId, relative, equalRelative), () -> updateRelative(dto, relative));

    }

    private void updateRelative(RelativeDto dto, Relative relative) {
        relative.setName(dto.name());
        relative.setAddress(dto.address());
        relative.setPhone(dto.phone());
        repository.save(relative);
    }

    private void replaceRelative(Principal principal, long childId, Relative relative, Relative equalRelative) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        child.addRelative(equalRelative);
        child.removeRelative(relative);
        childRepository.save(child);
    }
}
