package com.example.demo.controller.teacher;

import com.example.demo.dto.TeacherDto;
import com.example.demo.dto.TeacherFullDto;
import com.example.demo.dto.TeacherWithGroupDto;
import com.example.demo.exception.GroupContainsKidsException;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository repository;

    private final TeacherService teacherService;

    @PostMapping
    public void add(@RequestBody @Valid TeacherFullDto dto) {
        teacherService.save(dto.createTeacher());
    }

    @GetMapping("/all")
    public List<TeacherWithGroupDto> getAll() {
        return repository.findAllTeachersWithGroups().stream().map(TeacherWithGroupDto::new).toList();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public TeacherDto get(Principal principal) {
        return repository.findTeacherByEmail(principal.getName()).map(TeacherDto::new).
                orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(Principal principal, @RequestBody @Valid TeacherFullDto dto) {
        teacherService.update(principal.getName(), dto.email(), dto.password(), dto.name(), dto.skype(), dto.phone());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void remove(Principal principal) {
        if (repository.deleteTeacherByEmail(principal.getName()) == 0)
            throw new GroupContainsKidsException();
    }
}
