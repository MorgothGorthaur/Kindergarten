package com.example.demo.repository;

import com.example.demo.model.Child;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @EntityGraph(attributePaths = {"group", "relatives"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findChildrenByGroup_TeacherEmail(String email);


    /**
     * @param email the teacher`s email
     * @return a list of kids whose birthday is today or in the future,
     * sorted by month and day
     */
    @Query("""
            SELECT c FROM Child c WHERE c.group.teacher.email = ?1
            AND (MONTH(c.birthYear) > MONTH(CURRENT_DATE())
            OR (MONTH(c.birthYear) = MONTH(CURRENT_DATE()) AND DAY(c.birthYear) >= DAY(CURRENT_DATE())))
            ORDER BY MONTH(c.birthYear), DAY(c.birthYear)""")
    List<Child> findKidsThatWaitBirthDayByTeacherEmail(String email);

    /**
     * @param id the child`s ID
     * @return a list of related kids (having at least one common relative) with their groups and teachers.
     */
    @Query("SELECT c FROM Child c JOIN FETCH c.group.teacher t JOIN c.relatives r JOIN r.kids k WHERE k.id = ?1 AND c.id <> k.id")
    List<Child> findRelatedKidsWithTheirGroupsAndTeachers(long id);


    @EntityGraph(attributePaths = "relatives", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Child> findChildByIdAndGroup_TeacherEmail(long id, String email);

    @EntityGraph(attributePaths = "group", type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findAll();
}
