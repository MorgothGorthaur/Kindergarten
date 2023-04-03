package com.example.demo.controller.admin;

import com.example.demo.dto.AdminDto;
import com.example.demo.dto.AdminFullDto;
import com.example.demo.exception.AdminAlreadyExistException;
import com.example.demo.exception.AdminNotFoundException;
import com.example.demo.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/kindergarten/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PasswordEncoder encoder;

    private final AdminRepository repository;


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
    @GetMapping
    public AdminDto get(Principal principal) {
        return repository.findAdminByEmail(principal.getName())
                .map(AdminDto::new)
                .orElseThrow(() -> new AdminNotFoundException(principal.getName()));
    }

}
