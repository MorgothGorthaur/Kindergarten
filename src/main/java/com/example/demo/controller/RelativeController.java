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
        var child =  childRepository.getChildByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        return child.getRelatives().stream().map(mapper::toRelativeDto).toList();
    }

    @PostMapping("/{childId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RelativeDto add(Principal principal, @PathVariable long childId, @RequestBody RelativeDto dto) {
        var child =  childRepository.getChildByIdAndTeacherEmail(childId, principal.getName())
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()));
        var relative = dto.toRelative();
        child.addRelative(relative);
        return mapper.toRelativeDto(repository.save(relative));
    }

    @DeleteMapping("/{childId}/{relativeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void delete(Principal principal,  @PathVariable long childId, @PathVariable long relativeId) {
        var child =  childRepository.getChildByIdAndTeacherEmail(childId, principal.getName())
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
        relative.setAddress(dto.address());
        relative.setName(dto.name());
        relative.setPhoneNumber(dto.phone());
        repository.save(relative);
    }
}
