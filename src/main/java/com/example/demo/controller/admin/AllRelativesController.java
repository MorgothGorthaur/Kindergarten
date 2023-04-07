package com.example.demo.controller.admin;

import com.example.demo.dto.RelativeWithKidsAndGroups;
import com.example.demo.model.Relative;
import com.example.demo.repository.RelativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/kindergarten/admin/relatives")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AllRelativesController {
    private final RelativeRepository repository;

    @GetMapping
    public List<RelativeWithKidsAndGroups> getAll() {
        return repository.findAll().stream().map(RelativeWithKidsAndGroups::new).toList();
    }

    @GetMapping("/by_name")
    public List<RelativeWithKidsAndGroups> getAllSortedByName() {
        return repository.findAll().stream().sorted(Comparator.comparing(Relative::getName)).map(RelativeWithKidsAndGroups::new).toList();
    }

    @GetMapping("/by_kids")
    public List<RelativeWithKidsAndGroups> getAllSortedByKidsCount() {
        return repository.findAll().stream().sorted(Comparator.comparingInt(relative -> relative.getKids().size())).map(RelativeWithKidsAndGroups::new).toList();
    }

    @GetMapping("/by_address")
    public List<RelativeWithKidsAndGroups> getAllSortedByAddress() {
        return repository.findAll().stream().sorted(Comparator.comparing(Relative::getAddress)).map(RelativeWithKidsAndGroups::new).toList();
    }

}
