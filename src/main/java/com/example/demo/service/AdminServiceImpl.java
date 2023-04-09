package com.example.demo.service;

import com.example.demo.exception.AdminAlreadyExistException;
import com.example.demo.exception.AdminNotFoundException;
import com.example.demo.exception.TeacherAlreadyExistException;
import com.example.demo.model.Admin;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository repository;
    private final PasswordEncoder encoder;
    private final AccountRepository accountRepository;

    @Override
    public void save(Admin admin) {
        try {
            admin.setPassword(encoder.encode(admin.getPassword()));
            repository.save(admin);
        } catch (DataIntegrityViolationException ex) {
            throw new AdminAlreadyExistException(admin.getEmail());
        }
    }

    @Override
    public void update(String oldEmail, String newEmail, String newPassword, String newPhone) {
        if (!oldEmail.equals(newEmail) && accountRepository.findAccountByEmail(newEmail).isPresent())
            throw new TeacherAlreadyExistException(newEmail);
        var admin = repository.findAdminByEmail(oldEmail).orElseThrow(() -> new AdminNotFoundException(newEmail));
        admin.setEmail(newEmail);
        admin.setPassword(encoder.encode(newPassword));
        admin.setPhone(newPhone);
        repository.save(admin);
    }

    @Override
    public void delete(String email) {
        var admin = repository.findAdminByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException(email));
        repository.delete(admin);
    }
}
