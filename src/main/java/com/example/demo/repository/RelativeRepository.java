package com.example.demo.repository;

import com.example.demo.model.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RelativeRepository extends JpaRepository<Relative, Long> {

    /**
     * finds a relative with a child by the relative`s id, the child`s id and the teacher`s email
     */
    @Query("SELECT r FROM Relative r LEFT JOIN FETCH r.kids k WHERE r.id = ?1 AND k.id = ?2 AND k.group.teacher.email = ?3")
    Optional<Relative> findRelativeWithChild(long relativeId, long kidsId, String email);

    /**
     * finds relatives by the child`s id and the teacher`s email
     */
    @Query("SELECT r FROM Relative r JOIN r.kids k WHERE k.id = ?1 AND k.group.teacher.email = ?2")
    List<Relative> findRelatives(Long childId, String teacherEmail);

    /**
     * find`s a relative with the same name, address and phone
     */
    @Query("SELECT r FROM Relative r LEFT JOIN FETCH r.kids WHERE r.name = ?1 AND r.address = ?2 AND r.phone = ?3")
    Optional<Relative> findEqualRelativeWithKids(String name, String address, String phone);

    /**
     * find`s a relative with the same name, address and phone but with another id
     */
    @Query("SELECT r FROM Relative r LEFT JOIN FETCH r.kids WHERE r.name = ?1 AND r.address = ?2 AND r.phone = ?3 AND r.id <> ?4")
    Optional<Relative> findEqualRelativeWithKidsAndAnotherId(String name, String address, String phone, long id);
}
