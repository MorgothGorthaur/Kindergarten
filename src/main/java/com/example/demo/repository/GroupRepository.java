package com.example.demo.repository;

import com.example.demo.model.Actuality;
import com.example.demo.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select g from Group g")
    List<Group> getGroupsWithTeacher();

    List<Group> getGroupsByTeacherEmailAndTeacherActuality(String email, Actuality actuality);
}
