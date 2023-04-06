package com.example.demo.service;

import com.example.demo.exception.AdminAlreadyExistException;
import com.example.demo.exception.TeacherAlreadyExistException;
import com.example.demo.model.Admin;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdminRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public void update(String oldEmail, String newEmail, String newPassword, String newPhone) {
        if (repository.updateAdminByEmail(oldEmail, newPhone) != 1
                || accountRepository.updateAccountByEmail(oldEmail, newEmail, encoder.encode(newPassword)) != 1)
            throw new TeacherAlreadyExistException(oldEmail);
    }
}
