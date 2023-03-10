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
import java.util.Objects;

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
        var child =  childRepository.getChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        var relative = dto.toRelative();
        repository.findByAddress(relative.getAddress()).ifPresent(value -> relative.setId(value.getId()));
        child.addRelative(relative);
        return mapper.toRelativeDto(repository.save(relative));
    }

    @DeleteMapping("/{childId}/{relativeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void delete(Principal principal,  @PathVariable long childId, @PathVariable long relativeId) {
        var child =  childRepository.getChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        var relative = repository.findById(relativeId)
                        .orElseThrow(RelativeNotFoundException::new);
        child.removeRelative(relative);
        childRepository.save(child);
    }


    @PatchMapping("/{childId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(Principal principal,  @PathVariable long childId, @RequestBody RelativeDto dto) {
        var relative = repository.getRelative(dto.id(), childId, principal.getName())
                .orElseThrow(RelativeNotFoundException::new);
        var anotherRelativeWithSameAddres = repository.findByAddress(dto.address());
        if(anotherRelativeWithSameAddres.isPresent() && !anotherRelativeWithSameAddres.get().getId().equals(dto.id())) {
            var child = childRepository.getChildWithRelativesByIdAndTeacherEmail(childId, principal.getName())
                    .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
            child.removeRelative(relative);
            relative = dto.toRelative();
            relative.setId(anotherRelativeWithSameAddres.get().getId());
            child.addRelative(relative);
        } else {
            relative.setName(dto.name());
            relative.setAddress(dto.address());
            relative.setPhoneNumber(dto.phone());
        }
        repository.save(relative);
    }
}
