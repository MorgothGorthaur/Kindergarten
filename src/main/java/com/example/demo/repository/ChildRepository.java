package com.example.demo.repository;

import com.example.demo.model.Child;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query(value = "select children.id, children.name, children.birth_year, children.group_id from children \n" +
            "join `groups` g on g.id = children.group_id\n" +
            "join teachers t on g.id = t.group_id\n" +
            "where t.email = '?1'", nativeQuery = true)
    List<Child> getChildByTeacherEmail(String email);


}
