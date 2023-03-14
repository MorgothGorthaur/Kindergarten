package com.example.demo.repository;

import com.example.demo.model.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RelativeRepository extends JpaRepository<Relative, Long> {

    @Query("SELECT r FROM Relative r JOIN Child c JOIN c.group.teacher t WHERE r.id = ?1 AND c.id = ?2 AND t.email = ?3")
    Optional<Relative> findRelativeByRelativeIdAndChildIdAndTeacherEmail(long relativeId, long kidsId, String email);

    @Query("SELECT r FROM Relative r JOIN r.kids k JOIN k.group.teacher t WHERE k.id = ?1 AND t.email = ?2")
    List<Relative> findAllRelativesByChildIdAndTeacherEmail(Long childId, String teacherEmail);

    @Query("SELECT r FROM Relative r LEFT JOIN Child c WHERE r.name = ?1 AND r.address = ?2 AND r.phone = ?3")
    Optional<Relative> findEqualRelative(String name, String address, String phone);

    @Query("SELECT r FROM Relative r LEFT JOIN Child c WHERE r.name = ?1 AND r.address = ?2 AND r.phone = ?3 AND r.id <> ?4")
    Optional<Relative> findEqualRelativeWithAnotherId(String name, String address, String phone, long id);
}
