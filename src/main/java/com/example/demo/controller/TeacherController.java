package com.example.demo.controller;

import com.example.demo.dto.TeacherDto;
import com.example.demo.dto.TeacherFullDto;
import com.example.demo.dto.TeacherWithGroupDto;
import com.example.demo.exception.GroupContainsKidsException;
import com.example.demo.exception.TeacherAlreadyExist;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.repository.TeacherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    @PostMapping
    public void add(@RequestBody @Valid TeacherFullDto dto) {
        try{
            var teacher = dto.toTeacher();
            teacher.setPassword(encoder.encode(teacher.getPassword()));
            repository.save(teacher);
        } catch (DataIntegrityViolationException ex) {
            throw new TeacherAlreadyExist(dto.email());
        }
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
        if(repository.updateTeacherByEmail(principal.getName(), dto.email(), dto.name(), dto.skype(), dto.phone(), encoder.encode(dto.password())) == 0) throw new TeacherAlreadyExist(principal.getName());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void remove(Principal principal) {
        if(repository.deleteTeacherByEmailIfGroupDoesntContainsKids(principal.getName())==0) throw new GroupContainsKidsException();
    }
}
