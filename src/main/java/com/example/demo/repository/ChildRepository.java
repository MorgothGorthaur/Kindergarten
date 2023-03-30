package com.example.demo.repository;

import com.example.demo.model.Child;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query("SELECT c FROM Child c WHERE c.group.teacher.email = ?1")
    List<Child> findKidsByTeacherEmail(String email);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.relatives WHERE c.group.teacher.email = ?1")
    List<Child> findKidsWithRelativesByTeacherEmail(String email);

    /**
     * finds kids whose birthday is today or in the future, and sort the result by month and day.
     */
    @Query("""
            SELECT c FROM Child c WHERE c.group.teacher.email = ?1
            AND (MONTH(c.birthYear) > MONTH(CURRENT_DATE())
            OR (MONTH(c.birthYear) = MONTH(CURRENT_DATE()) AND DAY(c.birthYear) >= DAY(CURRENT_DATE())))
            ORDER BY MONTH(c.birthYear), DAY(c.birthYear)""")
    List<Child> findKidsThatWaitBirthDayByTeacherEmail(String email);

    /**
     * finds related kids (having at least one common relative) with their groups and teachers.
     */
    @Query("SELECT c FROM Child c JOIN FETCH c.group.teacher t JOIN c.relatives r JOIN r.kids k WHERE k.id = ?1 AND c.id <> k.id")
    List<Child> findRelatedKidsWithTheirGroupsAndTeachers(long id);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.relatives r WHERE c.id = ?1 AND c.group.teacher.email = ?2")
    Optional<Child> findChildWithRelativesByIdAndTeacherEmail(long childId, String teacherEmail);


    @Modifying
    @Transactional
    @Query("UPDATE Child c SET c.name = ?3, c.birthYear = ?4 WHERE c.id = ?2 AND EXISTS (SELECT t FROM Teacher t WHERE t.email = ?1 AND t.group.id = c.group.id)")
    int updateChildByIdAndTeacherEmail(String email, long id, String name, LocalDate birthYear);

    @Modifying
    @Transactional
    @Query("DELETE FROM Child c WHERE c.id = ?2 AND EXISTS (SELECT t FROM Teacher t WHERE t.email = ?1 AND t.group.id = c.group.id)")
    int deleteChildByIdAndTeacherEmail(String email, long id);
}
