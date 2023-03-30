package com.example.demo.repository;

import com.example.demo.model.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query(value = "SELECT t FROM Teacher t LEFT JOIN FETCH t.group.kids WHERE t.email = ?1")
    Optional<Teacher> findTeacherWithGroupAndKidsByEmail(String email);

    Optional<Teacher> findTeacherByEmail(String username);

    @Query(value = "SELECT t FROM Teacher t LEFT JOIN FETCH t.group")
    List<Teacher> findAllTeachersWithGroups();

    /**
     * updates the teacher if the table doesn't contain a teacher with the same email
     *
     * @param oldEmail    the teacher`s email
     * @param newEmail    the new email for the teacher
     * @param newName     the new name for the teacher
     * @param newSkype    the new Skype for the teacher
     * @param newPhone    the new phone number for the teacher
     * @param newPassword the new password for the teacher
     * @return 1 if the teacher was updated, or 0 if the teacher with the given oldEmail is not found
     * or if a teacher with the newEmail already exists in the database.
     */
    @Modifying
    @Transactional
    @Query("""
            UPDATE Teacher t SET t.email = ?2, t.name = ?3, t.skype = ?4, t.phone = ?5, t.password = ?6
            WHERE t.email = ?1 AND NOT EXISTS (SELECT t2 FROM Teacher t2 WHERE t2.email = ?2 AND t2.id <> t.id)""")
    int updateTeacherByEmail(String oldEmail, String newEmail, String newName, String newSkype, String newPhone, String newPassword);

    /**
     * deletes the teacher by email if the teacher`s group doesn`t contain any child
     *
     * @param email the teacher`s email
     * @return 1 if the teacher was deleted successfully, or 0 if the teacher with the given email is not found or has children in their group
     */

    @Modifying
    @Transactional
    @Query("DELETE FROM Teacher t WHERE t.email = ?1 AND NOT EXISTS (SELECT c FROM Child c WHERE c.group.teacher.email = ?1)")
    int deleteTeacherByEmail(String email);

}
