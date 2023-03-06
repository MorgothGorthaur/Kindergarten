package com.example.demo.repository;

import com.example.demo.model.Actuality;
import com.example.demo.model.Teacher;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query(value = "select t from Teacher t where t.actuality = 0")
    List<Teacher> findAll();

    Optional<Teacher> findTeacherByEmailAndActuality(String username, Actuality active);
}
