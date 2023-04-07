package com.example.demo.repository;

import com.example.demo.model.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @EntityGraph(attributePaths = {"kids"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Group> findGroupByTeacherEmail(String email);
}
