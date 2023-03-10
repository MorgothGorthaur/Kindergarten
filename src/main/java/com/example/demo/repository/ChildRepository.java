package com.example.demo.repository;

import com.example.demo.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query("SELECT c FROM Child c JOIN FETCH c.group.teacher t WHERE t.email = ?1")
    List<Child> getKidsByTeacherEmail(String email);
    @Query("SELECT c FROM Child c JOIN FETCH c.group.teacher t LEFT JOIN FETCH c.relatives WHERE t.email = ?1")
    List<Child> getKidsWithRelativesByTeacherEmail(String email);
    @Query("SELECT c FROM Child c JOIN FETCH c.group.teacher t WHERE c.id = ?1 AND t.email = ?2")
    Optional<Child> getChildByIdAndTeacherEmail(long id, String email);

    @Query(value = """
            SELECT children.*
            FROM children
            JOIN teachers ON children.group_id = teachers.group_id
            WHERE teachers.email = ?1 and DATE_ADD(birth_year, INTERVAL YEAR(CURRENT_DATE()) - YEAR(birth_year) YEAR) > CURRENT_DATE();""", nativeQuery = true)
    List<Child> getChildThatWaitBirthDay(String email);

    @Query("SELECT c FROM Child c JOIN FETCH c.relatives r JOIN FETCH r.kids k WHERE k.id = ?1 AND c.id <> k.id")
    List<Child> getBrothersAndSisters(long id);

    @Query("SELECT c FROM Child c JOIN FETCH c.relatives r WHERE c.id = ?1 AND c.group.teacher.email = ?2")
    Optional<Child> getChildWithRelativesByIdAndTeacherEmail(long childId, String teacherEmail);

    @Query("SELECT c FROM Child c JOIN FETCH c.group g JOIN FETCH g.teacher t LEFT JOIN FETCH c.relatives WHERE c.id = ?1 AND t.email = ?2")
    Optional<Child> getFullChildByTeacherEmail(long id, String email);



}
