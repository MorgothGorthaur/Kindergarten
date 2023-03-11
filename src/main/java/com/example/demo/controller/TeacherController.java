package com.example.demo.controller;

import com.example.demo.dto.Mapper;
import com.example.demo.dto.TeacherFullDto;
import com.example.demo.dto.TeacherWithGroupDto;
import com.example.demo.exception.GroupContainsKidsException;
import com.example.demo.exception.TeacherAlreadyExist;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.repository.TeacherRepository;
import jakarta.validation.Valid;
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
    public void add(@RequestBody @Valid TeacherFullDto dto) {
        var teacher = dto.toTeacher();
        teacher.setPassword(encoder.encode(teacher.getPassword()));
        if(repository.findTeacherByEmail(teacher.getEmail()).isEmpty()) repository.save(teacher);
        else throw new TeacherAlreadyExist(teacher.getEmail());
    }
    @GetMapping("/all")
    public List<TeacherWithGroupDto> getAll() {
        return repository.findAll().stream().map(mapper::toTeacherWithGroupDto).toList();
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(Principal principal, @RequestBody @Valid TeacherFullDto dto) {
        var teacher = repository.findTeacherByEmail(principal.getName())
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        if(repository.findTeachersWithSameEmailAndAnotherId(teacher.getId(), dto.email()).isEmpty()) {
            teacher.setName(dto.name());
            teacher.setSkype(dto.skype());
            teacher.setEmail(dto.email());
            teacher.setPassword(encoder.encode(dto.password()));
            teacher.setPhone(dto.phone());
            repository.save(teacher);
        } else throw new TeacherAlreadyExist(dto.email());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void remove(Principal principal) {
        var teacher = repository.findTeacherByEmail(principal.getName())
                .orElseThrow(() -> new TeacherNotFoundException(principal.getName()));
        teacher.removeGroup();
        repository.delete(teacher);
    }
}
