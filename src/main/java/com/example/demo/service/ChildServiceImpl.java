package com.example.demo.service;

import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.model.Child;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final GroupRepository groupRepository;
    private final ChildRepository repository;

    @Override
    public Child save(String email, Child child) {
        var group = groupRepository.findGroupByTeacherEmail(email)
                .orElseThrow(() -> new GroupNotFoundException(email));
        group.addChild(child);
        return repository.save(child);
    }
}
