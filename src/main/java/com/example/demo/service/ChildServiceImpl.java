package com.example.demo.service;

import com.example.demo.exception.kindergarten.notfound.ChildNotFoundException;
import com.example.demo.exception.kindergarten.notfound.GroupNotFoundException;
import com.example.demo.model.Child;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final GroupRepository groupRepository;
    private final ChildRepository repository;

    @Override
    public Child save(String email, Child child) {
        var group = groupRepository.findGroupAndKidsByTeacherEmail(email)
                .orElseThrow(() -> new GroupNotFoundException(email));
        group.addChild(child);
        return repository.save(child);
    }

    @Override
    public void update(String email, long id, String name, LocalDate birthYear) {
        var child = repository.findChildByIdAndGroup_TeacherEmail(id, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        child.setName(name);
        child.setBirthYear(birthYear);
        repository.save(child);
    }

    @Override
    public void delete(long id, String email) {
        var child = repository.findChildByIdAndGroup_TeacherEmail(id, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        repository.delete(child);
    }

}
