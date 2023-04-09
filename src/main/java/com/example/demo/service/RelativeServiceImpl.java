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
    public Relative save(String email, long childId, String name, String address, String phone) {
        var child = childRepository.findChildAndRelativesByIdAndGroup_TeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        var addedRelative = repository.findRelativeByNameAndPhoneAndAddress(name, phone, address)
                .orElseGet(() -> new Relative(name, phone, address));
        child.addRelative(addedRelative);
        return repository.save(addedRelative);
    }

    @Override
    public void delete(String email, long childId, long relativeId) {
        var child = childRepository.findChildAndRelativesByIdAndGroup_TeacherEmail(childId, email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        child.removeRelative(new Relative(relativeId));
        childRepository.save(child);
    }

    @Override
    public Relative updateOrReplaceRelative(String email, long childId, long relativeId, String name, String address, String phone) {
        var updatedRelative = repository.findRelativeWithChild(relativeId, childId, email)
                .orElseThrow(RelativeNotFoundException::new);
        return repository.findRelativeByNameAndPhoneAndAddressAndIdNot(name, phone, address, relativeId)
                .map(equalRelative -> replaceRelative(updatedRelative, equalRelative, email))
                .orElseGet(() -> updateRelative(name, address, phone, updatedRelative));
    }

    private Relative updateRelative(String name, String address, String phone, Relative updatedRelative) {
        updatedRelative.setName(name);
        updatedRelative.setAddress(address);
        updatedRelative.setPhone(phone);
        return repository.save(updatedRelative);
    }

    private Relative replaceRelative(Relative updatedRelative, Relative equalRelative, String email) {
        var child = updatedRelative.getKids().stream().findFirst()
                .orElseThrow(() -> new ChildNotFoundException(email));
        child.addRelative(equalRelative);
        child.removeRelative(updatedRelative);
        childRepository.save(child);
        return equalRelative;
    }
}
