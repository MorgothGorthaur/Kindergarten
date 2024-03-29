package com.example.demo.controller.teacher;

import com.example.demo.dto.RelativeDto;
import com.example.demo.exception.kindergarten.notfound.ChildNotFoundException;
import com.example.demo.model.Child;
import com.example.demo.repository.ChildRepository;
import com.example.demo.service.RelativeService;
import jakarta.validation.Valid;
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
    private final ChildRepository childRepository;
    private final RelativeService service;

    @GetMapping("/{childId}")
    public List<RelativeDto> getAll(Principal principal, @PathVariable long childId) {
        return childRepository.findChildAndRelativesByIdAndGroup_TeacherEmail(childId, principal.getName())
                .map(Child::getRelatives)
                .orElseThrow(() -> new ChildNotFoundException(principal.getName()))
                .stream().map(RelativeDto::new).toList();
    }

    @PostMapping("/{childId}")
    public RelativeDto add(Principal principal, @PathVariable long childId, @RequestBody @Valid RelativeDto dto) {
        return new RelativeDto(service.save(principal.getName(), childId, dto.name(), dto.address(), dto.phone()));
    }

    @DeleteMapping("/{childId}/{relativeId}")
    public void delete(Principal principal, @PathVariable long childId, @PathVariable long relativeId) {
        service.delete(principal.getName(), childId, relativeId);
    }


    @PatchMapping("/{childId}")
    public RelativeDto update(Principal principal, @PathVariable long childId, @RequestBody @Valid RelativeDto dto) {
        return new RelativeDto(service.updateOrReplaceRelative(principal.getName(), childId, dto.id(), dto.name(), dto.address(), dto.phone()));
    }
}
