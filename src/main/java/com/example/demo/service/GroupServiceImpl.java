package com.example.demo.service;

import com.example.demo.exception.GroupCantBeUpdatedException;
import com.example.demo.exception.GroupNotFoundException;
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
    public void save(String email, Group group) {
        var teacher = teacherRepository.findTeacherByEmail(email)
                .orElseThrow(() -> new TeacherNotFoundException(email));
        teacher.addGroup(group);
        repository.save(group);
    }

    @Override
    public void update(String email, String name, int maxSize) {
        var group = repository.findGroupByTeacherEmail(email)
                .orElseThrow(() -> new GroupNotFoundException(email));
        if (group.isAbleToBeUpdated(maxSize)) {
            group.setName(name);
            group.setMaxSize(maxSize);
            repository.save(group);
        } else throw new GroupCantBeUpdatedException(maxSize);
    }

    @Override
    public void delete(String email) {
        var teacher = repository.findGroupByTeacherEmail(email)
                .map(Group::getTeacher)
                .orElseThrow(() -> new GroupNotFoundException(email));
        teacher.deleteGroup();
        teacherRepository.save(teacher);
    }
}
