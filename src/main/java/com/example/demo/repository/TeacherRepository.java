package com.example.demo.repository;

import com.example.demo.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {


    Optional<Teacher> findTeacherByEmail(String username);

    @Query(value = "SELECT t FROM Teacher t WHERE t.id <> ?1 AND t.email = ?2")
    Optional<Teacher> findTeachersWithSameEmailAndAnotherId(long id, String email);

}
