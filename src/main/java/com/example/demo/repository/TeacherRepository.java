package com.example.demo.repository;

import com.example.demo.model.Actuality;
import com.example.demo.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findTeacherByEmailAndActuality(String username, Actuality active);
}
