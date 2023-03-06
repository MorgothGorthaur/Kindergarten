package com.example.demo.controller;

import com.example.demo.dto.Mapper;
import com.example.demo.dto.TeacherDto;
import com.example.demo.dto.TeacherFullDto;
import com.example.demo.dto.TeacherWithGroupDto;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.Actuality;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final PasswordEncoder encoder;
    private final TeacherRepository repository;
    private final Mapper mapper;

    @PostMapping
    public void addTeacher(@RequestBody TeacherFullDto dto) {
        var teacher = dto.toTeacher();
        teacher.setPassword(encoder.encode(teacher.getPassword()));
        repository.save(teacher);
    }
    @GetMapping("/all")
    public List<TeacherWithGroupDto> getAll() {
        return repository.findAll().stream().map(mapper::toTeacherWithGroupDto).toList();
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateTeacher(Principal principal, @RequestBody TeacherFullDto dto) {
        var teacher = repository.findTeacherByEmailAndActuality(principal.getName(), Actuality.ACTIVE)
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.setName(dto.name());
        teacher.setSkype(dto.skype());
        teacher.setEmail(dto.email());
        teacher.setPassword(encoder.encode(dto.password()));
        teacher.setPhone(dto.phone());
        repository.save(teacher);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeTeacher(Principal principal) {
        var teacher = repository.findTeacherByEmailAndActuality(principal.getName(), Actuality.ACTIVE)
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.setActuality(Actuality.REMOVED);
        teacher.removeGroup();
        repository.save(teacher);
    }
}
