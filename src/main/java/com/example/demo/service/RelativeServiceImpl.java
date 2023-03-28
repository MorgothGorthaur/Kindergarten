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
        var addedRelative = repository.findEqualRelativeWithKids(name, phone, address).orElse(new Relative(name, phone, address));
        child.addRelative(addedRelative);
        return repository.save(addedRelative);
    }

    @Override
    public void delete(String email, long childId, long relativeId) {
         var relative = repository.findRelativeWithChild(relativeId, childId, email)
                .orElseThrow(RelativeNotFoundException::new);
        var child = relative.getKids().stream().toList().get(0);
        child.removeRelative(relative);
        childRepository.save(child);
    }

    @Override
    public Relative updateOrReplaceRelative(String email, long childId, long relativeId, String name, String address, String phone) {
        var updatedRelative = repository.findRelativeWithChild(relativeId, childId, email)
                .orElseThrow(RelativeNotFoundException::new);
        return repository.findEqualRelativeWithKidsAndAnotherId(name, phone, address, relativeId)
                .map(equalRelative -> replaceRelative(updatedRelative, equalRelative))
                .orElseGet(() -> updateRelative(name, address, phone, updatedRelative));
    }

    private Relative updateRelative(String name, String address, String phone, Relative updatedRelative) {
        updatedRelative.setName(name);
        updatedRelative.setAddress(address);
        updatedRelative.setPhone(phone);
        return repository.save(updatedRelative);
    }

    private Relative replaceRelative(Relative updatedRelative, Relative equalRelative) {
        var child = updatedRelative.getKids().stream().toList().get(0);
        child.addRelative(equalRelative);
        child.removeRelative(updatedRelative);
        childRepository.save(child);
        return equalRelative;
    }
}
