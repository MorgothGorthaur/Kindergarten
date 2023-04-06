package com.example.demo.controller.admin;

import com.example.demo.dto.AdminDto;
import com.example.demo.dto.AdminFullDto;
import com.example.demo.exception.AdminAlreadyExistException;
import com.example.demo.exception.AdminNotFoundException;
import com.example.demo.exception.TeacherAlreadyExistException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdminRepository;
import jakarta.transaction.Transactional;
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

    private final AccountRepository accountRepository;


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
    @PatchMapping
    @Transactional
    public void updateAdmin(Principal principal, @RequestBody AdminFullDto dto) {
        if (repository.updateAdminByEmail(principal.getName(), dto.phone()) != 1
                || accountRepository.updateAccountByEmail(principal.getName(), dto.email(), encoder.encode(dto.password())) != 1)
            throw new TeacherAlreadyExistException(dto.email());
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
