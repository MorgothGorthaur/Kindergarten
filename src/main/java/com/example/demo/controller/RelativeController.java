package com.example.demo.controller;

import com.example.demo.dto.Mapper;
import com.example.demo.dto.RelativeDto;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.exception.RelativeNotFoundException;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.RelativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        return repository.getAllRelativesByChildIdAndTeacherEmail(childId, principal.getName()).stream().map(mapper::toRelativeDto).toList();
    }

    @PostMapping("/{childId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RelativeDto add(Principal principal, @PathVariable long childId, @RequestBody RelativeDto dto) {
        var child = childRepository.getChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        var relative = dto.toRelative();
        repository.findEqualRelative(relative.getName(), relative.getAddress(), relative.getPhoneNumber())
                .ifPresent(value -> relative.setId(value.getId()));
        child.addRelative(relative);
        return mapper.toRelativeDto(repository.save(relative));
    }

    @DeleteMapping("/{childId}/{relativeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void delete(Principal principal, @PathVariable long childId, @PathVariable long relativeId) {
        var child = childRepository.getChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        var relative = repository.findById(relativeId)
                .orElseThrow(RelativeNotFoundException::new);
        child.removeRelative(relative);
        childRepository.save(child);
    }


    @PatchMapping("/{childId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(Principal principal, @PathVariable long childId, @RequestBody RelativeDto dto) {
        var relative = repository.getRelativeByRelativeIdAndChildIdAndTeacherEmail(dto.id(), childId, principal.getName())
                .orElseThrow(RelativeNotFoundException::new);
        var anotherRelativeWithSameAddress = repository.findEqualRelative(dto.name(), dto.address(), dto.phone());
        if (anotherRelativeWithSameAddress.isPresent() && !anotherRelativeWithSameAddress.get().getId().equals(dto.id())) {
            var child = childRepository.getChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                    .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
            child.addRelative(anotherRelativeWithSameAddress.get());
            child.removeRelative(relative);
            repository.delete(relative);
            childRepository.save(child);
        } else {
            relative.setName(dto.name());
            relative.setAddress(dto.address());
            relative.setPhoneNumber(dto.phone());
            repository.save(relative);
        }
    }
}
