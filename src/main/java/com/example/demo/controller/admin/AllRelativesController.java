package com.example.demo.controller.admin;

import com.example.demo.dto.RelativeWithKidsAndGroups;
import com.example.demo.repository.RelativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kindergarten/admin/relatives")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AllRelativesController {
    private final RelativeRepository repository;

    @GetMapping
    public List<RelativeWithKidsAndGroups> getAll() {
        return repository.findAllRelativesWithKidsAndTeachers().stream().map(RelativeWithKidsAndGroups::new).toList();
    }

    @GetMapping("/by_name")
    public List<RelativeWithKidsAndGroups> getAllSortedByName() {
        return repository.findAllRelativesWithKidsAndTeachersOrderByName().stream().map(RelativeWithKidsAndGroups::new).toList();
    }

    @GetMapping("/by_kids")
    public List<RelativeWithKidsAndGroups> getAllSortedByKidsCount() {
        return repository.findAllRelativesWithKidsAndTeachersOrderByKidCount().stream().map(RelativeWithKidsAndGroups::new).toList();
    }

    @GetMapping("/by_address")
    public List<RelativeWithKidsAndGroups> getAllSortedByAddress() {
        return repository.findAllRelativesWithKidsAndTeachersOrderByAddress().stream().map(RelativeWithKidsAndGroups::new).toList();
    }

}
