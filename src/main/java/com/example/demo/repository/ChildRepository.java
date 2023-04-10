package com.example.demo.repository;

import com.example.demo.model.Child;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findChildrenByGroup_TeacherEmail(String email);

    @EntityGraph(attributePaths = "relatives", type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findChildrenAndRelativesByGroup_TeacherEmail(String email);

    /**
     * @param id the child`s ID
     * @return a list of related kids (having at least one common relative) with their groups and teachers.
     */
    @Query("SELECT c FROM Child c JOIN FETCH c.group.teacher t JOIN c.relatives r JOIN r.kids k WHERE k.id = ?1 AND c.id <> k.id")
    List<Child> findRelatedKidsWithTheirGroupsAndTeachers(long id);

    Optional<Child> findChildByIdAndGroup_TeacherEmail(long id, String email);

    @EntityGraph(attributePaths = "relatives", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Child> findChildAndRelativesByIdAndGroup_TeacherEmail(long id, String email);

    @EntityGraph(attributePaths = "group", type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findAll();

    @EntityGraph(attributePaths = "group", type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findAllByOrderByGroupNameAsc();

    @EntityGraph(attributePaths = "group", type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findAllByOrderByGroup_TeacherEmail();


    @EntityGraph(attributePaths = "group", type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findAllByOrderByName();

    @EntityGraph(attributePaths = "group", type = EntityGraph.EntityGraphType.LOAD)
    List<Child> findAllByOrderByBirthYear();

}
