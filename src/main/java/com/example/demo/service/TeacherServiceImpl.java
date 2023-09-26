package com.example.demo.service;

import com.example.demo.exception.kindergarten.notacceptable.AccountAlreadyExistException;
import com.example.demo.exception.kindergarten.notfound.TeacherNotFoundException;
import com.example.demo.model.Teacher;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository repository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;

    @Override
    public void save(Teacher teacher) {
        try {
            teacher.setPassword(encoder.encode(teacher.getPassword()));
            repository.save(teacher);
        } catch (DataIntegrityViolationException ex) {
            throw new AccountAlreadyExistException(teacher.getEmail());
        }
    }

    @Override
    public void update(String oldEmail, String newEmail, String newPassword, String newName, String newSkype, String newPhone) {
        if (!oldEmail.equals(newEmail) && accountRepository.findAccountByEmail(newEmail).isPresent())
            throw new AccountAlreadyExistException(newEmail);
        var teacher = repository.findTeacherByEmail(oldEmail).orElseThrow(() -> new TeacherNotFoundException(oldEmail));
        teacher.setEmail(newEmail);
        teacher.setPassword(encoder.encode(newPassword));
        teacher.setName(newName);
        teacher.setPhone(newPhone);
        teacher.setSkype(newSkype);
        repository.save(teacher);
    }

    @Override
    public void delete(String email) {
        var teacher = repository.findTeacherAndGroupAndKidsByEmail(email)
                .orElseThrow(() -> new TeacherNotFoundException(email));
        teacher.deleteGroup();
        repository.delete(teacher);
    }
}
