package com.example.demo.repository;

import com.example.demo.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query("SELECT c FROM Child c JOIN c.group.teacher t WHERE t.email = ?1")
    List<Child> findKidsByTeacherEmail(String email);

    @Query("SELECT c FROM Child c JOIN c.group.teacher t LEFT JOIN FETCH c.relatives WHERE t.email = ?1")
    List<Child> findKidsWithRelativesByTeacherEmail(String email);

    @Query("SELECT c FROM Child c JOIN c.group.teacher t WHERE c.id = ?1 AND t.email = ?2")
    Optional<Child> findChildByIdAndTeacherEmail(long id, String email);

    @Query("SELECT c FROM Child c JOIN c.group.teacher t WHERE t.email = ?1" +
            " AND (MONTH(c.birthYear) > MONTH(CURRENT_DATE())" +
            " OR (MONTH(c.birthYear) = MONTH(CURRENT_DATE()) AND DAY(c.birthYear) > DAY(CURRENT_DATE())))")
    List<Child> findChildThatWaitBirthDay(String email);

    @Query("SELECT c FROM Child c JOIN FETCH c.group.teacher t JOIN FETCH c.relatives r JOIN FETCH r.kids k WHERE k.id = ?1 AND c.id <> k.id")
    List<Child> findBrothersAndSisters(long id);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.relatives r JOIN c.group.teacher t WHERE c.id = ?1 AND t.email = ?2")
    Optional<Child> findChildWithRelativesByIdAndTeacherEmail(long childId, String teacherEmail);


}
