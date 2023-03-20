package com.example.demo.service;

import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.Group;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final TeacherRepository teacherRepository;
    private final GroupRepository repository;
    @Override
    public void add(String email, Group group) {
        var teacher = teacherRepository.findTeacherByEmail(email)
                .orElseThrow(() -> new TeacherNotFoundException(email));
        teacher.addGroup(group);
        repository.save(group);
    }
}
