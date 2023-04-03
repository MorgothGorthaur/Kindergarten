package com.example.demo.controller.admin;

import com.example.demo.dto.ChildDto;
import com.example.demo.dto.ChildWithGroupAndTeacherDto;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.repository.ChildRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kindergarten/admin/kids")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class KidsController {
    private final ChildRepository repository;

    @PatchMapping
    public void updateChild(@RequestBody @Valid ChildDto dto) {
        if (repository.updateChild(dto.id(), dto.name(), dto.birthYear()) == 0)
            throw new ChildNotFoundException(dto.id());
    }

    @DeleteMapping("/{id}")
    public void deleteChild(@PathVariable long id) {
        if (repository.deleteById(id) == 0)
            throw new ChildNotFoundException(id);

    }

    @GetMapping("/kids")
    public List<ChildWithGroupAndTeacherDto> get() {
        return repository.findAllWithGroupsAndTeachers().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @GetMapping("/kids/group_name")
    public List<ChildWithGroupAndTeacherDto> getByGroupName() {
        return repository.findAllWithGroupsAndTeachersSortedByGroupName().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @GetMapping("/kids/teacher_email")
    public List<ChildWithGroupAndTeacherDto> getByTeacherName() {
        return repository.findAllWithGroupsAndTeachersSortedByTeacherEmail().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @GetMapping("/kids/name")
    public List<ChildWithGroupAndTeacherDto> getByName() {
        return repository.findAllWithGroupsAndTeachersSortedByName().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }


    @GetMapping("/kids/birth")
    public List<ChildWithGroupAndTeacherDto> getByBirth() {
        return repository.findAllWithGroupsAndTeachersSortedByBirth().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }
}
