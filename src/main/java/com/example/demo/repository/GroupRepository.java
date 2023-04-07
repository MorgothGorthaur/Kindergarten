package com.example.demo.repository;

import com.example.demo.model.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @EntityGraph(attributePaths = {"kids"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Group> findGroupByTeacherEmail(String email);

    /**
     * deletes the group of the teacher if it doesn`t contain any child
     *
     * @param email the teacher`s email
     * @return 1 if the group was deleted or 0 if the teacher has children in the group
     * or if the teacher with this email is not found.
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE teachers
            JOIN accounts ON teachers.account_id = accounts.id
            LEFT JOIN children ON teachers.group_id = children.group_id
            SET teachers.group_id = NULL
            WHERE accounts.email = ?1 AND children.group_id IS NULL;
            """, nativeQuery = true)
    int deleteGroupIfEmptyByTeacherEmail(String email);


    /**
     * updates the group by the teacher`s email, if quantity of kids in this group is lower than the new maxSize
     *
     * @param email   the teacher`s email
     * @param name    the new name for the group
     * @param maxSize the new maximum size for the group
     * @return 1 if the group was updated or 0 if the current
     * number of kids in the group is greater than the new maxSize
     * or if the teacher with this email is not found.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.name = ?2, g.maxSize = ?3 WHERE SIZE(g.kids) <= ?3 AND EXISTS (SELECT t FROM Teacher t WHERE t.email = ?1 AND t.group.id = g.id)")
    int updateGroupByTeacherEmail(String email, String name, int maxSize);
}
