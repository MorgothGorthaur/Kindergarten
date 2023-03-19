package com.example.demo.repository;

import com.example.demo.enums.Role;
import com.example.demo.model.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query(value = "SELECT t FROM Teacher t LEFT JOIN FETCH t.group.kids WHERE t.email = ?1")
    Optional<Teacher> findTeacherWithGroupAndKidsByEmail(String email);

    Optional<Teacher> findTeacherByEmail(String username);

    @Query(value = "SELECT t FROM Teacher t WHERE t.id <> ?1 AND t.email = ?2")
    Optional<Teacher> findTeachersWithSameEmailAndAnotherId(long id, String email);

    @Query(value = "SELECT t FROM Teacher t LEFT JOIN FETCH t.group")
    List<Teacher> findAllTeachersWithGroups();
    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.email = ?2, t.name = ?3, t.skype = ?4, t.phone = ?5, t.password = ?6  WHERE t.email = ?1 AND NOT EXISTS (SELECT t2 FROM Teacher t2 WHERE t2.email = ?2 AND t2.id <> t.id)")
    int updateTeacherByEmail(String oldEmail, String newEmail, String newName, String newSkype, String newPhone, String newPassword);


    @Modifying
    @Transactional
    @Query("DELETE FROM Teacher t WHERE t.email = ?1 AND NOT EXISTS (SELECT c FROM Child c WHERE c.group.teacher.email = ?1)")
    int deleteTeacherWithGroupByEmail(String email);

}
