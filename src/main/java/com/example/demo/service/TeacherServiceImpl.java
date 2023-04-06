package com.example.demo.service;

import com.example.demo.exception.TeacherAlreadyExistException;
import com.example.demo.model.Teacher;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository repository;

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void save(Teacher teacher) {
        try {
            teacher.setPassword(encoder.encode(teacher.getPassword()));
            repository.save(teacher);
        } catch (DataIntegrityViolationException ex) {
            throw new TeacherAlreadyExistException(teacher.getEmail());
        }
    }

    @Override
    @Transactional
    public void update(String oldEmail, String newEmail, String newPassword, String newName, String newSkype, String newPhone) {
        if (repository.updateTeacherByEmail(oldEmail, newName, newSkype, newPhone) != 1
                || accountRepository.updateAccountByEmail(oldEmail, newEmail, encoder.encode(newPassword)) != 1)
            throw new TeacherAlreadyExistException(newEmail);
    }
}
