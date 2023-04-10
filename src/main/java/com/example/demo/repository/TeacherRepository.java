package com.example.demo.repository;

import com.example.demo.model.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findTeacherByEmail(String username);

    @EntityGraph(attributePaths = "group.kids", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Teacher> findTeacherAndGroupAndKidsByEmail(String email);

    @EntityGraph(attributePaths = {"group"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Teacher> findAll();

}
