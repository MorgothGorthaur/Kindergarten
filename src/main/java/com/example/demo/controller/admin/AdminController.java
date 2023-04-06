package com.example.demo.controller.admin;

import com.example.demo.dto.AdminDto;
import com.example.demo.dto.AdminFullDto;
import com.example.demo.exception.AdminNotFoundException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
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

    private final AccountRepository accountRepository;

    private final AdminService service;


    @PostMapping
    public void addAdmin(@RequestBody AdminFullDto dto) {
        service.save(dto.createAdmin());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping
    public void updateAdmin(Principal principal, @RequestBody AdminFullDto dto) {
        service.update(principal.getName(), dto.email(), dto.password(), dto.phone());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public void deleteAdmin(Principal principal) {
        if (repository.deleteAdminByEmail(principal.getName()) == 0)
            throw new AdminNotFoundException(principal.getName());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public AdminDto get(Principal principal) {
        return repository.findAdminByEmail(principal.getName())
                .map(AdminDto::new)
                .orElseThrow(() -> new AdminNotFoundException(principal.getName()));
    }

}
