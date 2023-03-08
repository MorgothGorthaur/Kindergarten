package com.example.demo.repository;

import com.example.demo.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query(value = "select children.* from children \n" +
            "join teachers t on children.group_id = t.group_id\n" +
            "where t.email = ?1", nativeQuery = true)
    List<Child> getKidsByTeacherEmail(String email);
    @Query(value = "select children.* from children \n" +
            "join teachers t on children.group_id = t.group_id\n" +
            "where children.id = ?1 and t.email = ?2", nativeQuery = true)
    Optional<Child> getChildByIdAndTeacherEmail(long id, String email);

}
