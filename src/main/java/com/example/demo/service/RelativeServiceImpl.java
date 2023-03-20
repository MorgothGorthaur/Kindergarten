package com.example.demo.service;

import com.example.demo.dto.RelativeDto;
import com.example.demo.exception.ChildNotFoundException;
import com.example.demo.exception.RelativeNotFoundException;
import com.example.demo.model.Relative;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.RelativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelativeServiceImpl implements RelativeService {
    private final ChildRepository childRepository;
    private final RelativeRepository repository;

    @Override
    public Relative add(String email, long childId, Relative relative) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        var addedRelative = repository.findEqualRelative(relative.getName(), relative.getAddress(), relative.getPhone()).orElse(relative);
        child.addRelative(addedRelative);
        return repository.save(addedRelative);
    }

    @Override
    public void delete(String email, long childId, long relativeId) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        var relative = repository.findRelativeByIdAndChildIdAndTeacherEmail(relativeId, childId, email)
                .orElseThrow(RelativeNotFoundException::new);
        child.removeRelative(relative);
        childRepository.save(child);
    }

    @Override
    public void updateOrReplaceRelative(String email, long childId, long relativeId, Relative relative) {
        var updatedRelative = repository.findRelativeByIdAndChildIdAndTeacherEmail(relativeId, childId, email)
                .orElseThrow(RelativeNotFoundException::new);
        repository.findEqualRelativeWithAnotherId(relative.getName(), relative.getAddress(), relative.getPhone(), relativeId)
                .ifPresentOrElse(equalRelative -> replaceRelative(email, childId, updatedRelative, equalRelative), () -> updateRelative(relative, updatedRelative));
    }

    private void updateRelative(Relative relative, Relative updatedRelative) {
        updatedRelative.setName(relative.getName());
        updatedRelative.setAddress(relative.getAddress());
        updatedRelative.setPhone(relative.getAddress());
        repository.save(relative);
    }

    private void replaceRelative(String email, long childId, Relative updatedRelative, Relative equalRelative) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        child.addRelative(equalRelative);
        child.removeRelative(updatedRelative);
        childRepository.save(child);
    }
}
