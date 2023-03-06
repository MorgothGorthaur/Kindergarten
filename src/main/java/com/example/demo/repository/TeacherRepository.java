package com.example.demo.repository;

import com.example.demo.model.Actuality;
import com.example.demo.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query(value = "select t from Teacher t where t.actuality = 0")
    List<Teacher> findAll();

    Optional<Teacher> findTeacherByEmailAndActuality(String username, Actuality active);

    @Query(value = "select t from Teacher t where t.actuality = 0 and t.id <> ?1 and t.email = ?2")
    Optional<Teacher> findTeachersWithSameEmailAndAnotherId(long id, String email);

}
