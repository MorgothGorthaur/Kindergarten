package com.example.demo.repository;

import com.example.demo.model.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.group = NULL  WHERE t.email = ?1 AND NOT EXISTS (SELECT c FROM Child c WHERE c.group.teacher.email = ?1)")
    int deleteGroupFromTeacherIfGroupDoesntContainsKids(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.name = ?2, g.maxSize = ?3 WHERE SIZE(g.kids) <= ?3 AND EXISTS (SELECT t FROM Teacher t WHERE t.email = ?1 AND t.group.id = g.id)")
    int updateGroup(String email, String name, int maxSize);
}
