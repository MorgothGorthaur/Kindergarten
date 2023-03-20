package com.example.demo.service;

import com.example.demo.dto.ChildDto;
import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.model.Child;
import com.example.demo.model.Teacher;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final TeacherRepository teacherRepository;
    private final ChildRepository repository;
    @Override
    public Child add(String email, Child child) {
        var group = teacherRepository.findTeacherWithGroupAndKidsByEmail(email)
                .map(Teacher::getGroup)
                .orElseThrow(() -> new GroupNotFoundException(email));
        group.addChild(child);
        return repository.save(child);
    }
}
