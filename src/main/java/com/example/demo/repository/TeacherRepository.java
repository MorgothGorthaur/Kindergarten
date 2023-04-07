package com.example.demo.repository;

import com.example.demo.model.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findTeacherByEmail(String username);

    @EntityGraph(attributePaths = {"group"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Teacher> findAll();

    /**
     * updates the teacher if the table doesn't contain a teacher with the same email
     *
     * @param oldEmail the teacher`s email
     * @param newName  the new name for the teacher
     * @param newSkype the new Skype for the teacher
     * @param newPhone the new phone number for the teacher
     * @return 1 if the teacher was updated, or 0 if the teacher with the given oldEmail is not found
     * or if a teacher with the newEmail already exists in the database.
     */
    @Modifying
    @Query(value = """
            UPDATE teachers t
            JOIN accounts a ON t.account_id = a.id
            SET t.name = ?2, t.skype = ?3, t.phone_number = ?4
            WHERE a.email = ?1
            """, nativeQuery = true)
    int updateTeacherByEmail(String oldEmail, String newName, String newSkype, String newPhone);

    /**
     * deletes the teacher by email if the teacher`s group doesn`t contain any child
     *
     * @param email the teacher`s email
     * @return 1 if the teacher was deleted successfully, or 0 if the teacher with the given email is not found or has children in their group
     */

    @Modifying
    @Transactional
    @Query(value = """
            DELETE teachers FROM teachers
            JOIN accounts ON teachers.account_id = accounts.id
            LEFT JOIN children ON teachers.group_id = children.group_id
            WHERE accounts.email = ?1 AND children.group_id IS NULL
            """, nativeQuery = true)
    int deleteTeacherByEmail(String email);

}
