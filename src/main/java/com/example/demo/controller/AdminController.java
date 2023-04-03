package com.example.demo.controller;

import com.example.demo.dto.AdminDto;
import com.example.demo.dto.AdminFullDto;
import com.example.demo.dto.ChildDto;
import com.example.demo.dto.ChildWithGroupAndTeacherDto;
import com.example.demo.exception.AdminAlreadyExistException;
import com.example.demo.exception.AdminNotFoundException;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ChildRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PasswordEncoder encoder;

    private final AdminRepository repository;

    private final ChildRepository childRepository;

    @PostMapping
    public void addAdmin(@RequestBody AdminFullDto dto) {
        try {
            var admin = dto.createAdmin();
            admin.setPassword(encoder.encode(admin.getPassword()));
            repository.save(admin);
        } catch (DataIntegrityViolationException ex) {
            throw new AdminAlreadyExistException(dto.email());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/kids")
    public void updateChild(@RequestBody @Valid ChildDto dto) {
        if(childRepository.updateChild(dto.id(), dto.name(), dto.birthYear()) == 0)
            throw new ChildNotFoundException(dto.id());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/kids/{id}")
    public void updateChild(@PathVariable long id) {
        childRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public AdminDto get(Principal principal) {
        return repository.findAdminByEmail(principal.getName())
                .map(AdminDto::new)
                .orElseThrow(() -> new AdminNotFoundException(principal.getName()));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/kids")
    public List<ChildWithGroupAndTeacherDto> get() {
        return childRepository.findAllWithGroupsAndTeachers().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/kids/group_name")
    public List<ChildWithGroupAndTeacherDto> getByGroupName() {
        return childRepository.findAllWithGroupsAndTeachersSortedByGroupName().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/kids/teacher_email")
    public List<ChildWithGroupAndTeacherDto> getByTeacherName() {
        return childRepository.findAllWithGroupsAndTeachersSortedByTeacherEmail().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/kids/name")
    public List<ChildWithGroupAndTeacherDto> getByName() {
        return childRepository.findAllWithGroupsAndTeachersSortedByName().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/kids/birth")
    public List<ChildWithGroupAndTeacherDto> getByBirth() {
        return childRepository.findAllWithGroupsAndTeachersSortedByBirth().stream().map(ChildWithGroupAndTeacherDto::new).toList();
    }

}
