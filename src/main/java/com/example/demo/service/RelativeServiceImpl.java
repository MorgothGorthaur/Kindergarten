package com.example.demo.service;

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
    public Relative add(String email, long childId, String name, String address, String phone) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        var addedRelative = repository.findEqualRelativeWithKids(name, address, phone).orElse(new Relative(name, address, phone));
        child.addRelative(addedRelative);
        return repository.save(addedRelative);
    }

    @Override
    public void delete(String email, long childId, long relativeId) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        var relative = repository.findRelativeWithChild(relativeId, childId, email)
                .orElseThrow(RelativeNotFoundException::new);
        child.removeRelative(relative);
        childRepository.save(child);
    }

    @Override
    public void updateOrReplaceRelative(String email, long childId, long relativeId, String name, String address, String phone) {
        var updatedRelative = repository.findRelativeWithChild(relativeId, childId, email)
                .orElseThrow(RelativeNotFoundException::new);
        repository.findEqualRelativeWithKidsAndAnotherId(name, address, phone, relativeId).ifPresentOrElse(
                equalRelative -> replaceRelative(email, childId, updatedRelative, equalRelative),
                () -> updateRelative(name, address, phone, updatedRelative));
    }

    private void updateRelative(String name, String address, String phone, Relative updatedRelative) {
        updatedRelative.setName(name);
        updatedRelative.setAddress(address);
        updatedRelative.setPhone(phone);
        repository.save(updatedRelative);
    }

    private void replaceRelative(String email, long childId, Relative updatedRelative, Relative equalRelative) {
        var child = childRepository.findChildWithRelativesByIdAndTeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        child.addRelative(equalRelative);
        child.removeRelative(updatedRelative);
        childRepository.save(child);
    }
}
