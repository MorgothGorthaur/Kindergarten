package com.example.demo.service;

import com.example.demo.exception.TeacherAlreadyExistException;
import com.example.demo.model.Teacher;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository repository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void add(Teacher teacher) {
        try {
            teacher.setPassword(encoder.encode(teacher.getPassword()));
            repository.save(teacher);
        } catch (DataIntegrityViolationException ex) {
            throw new TeacherAlreadyExistException(teacher.getEmail());
        }
    }
}
