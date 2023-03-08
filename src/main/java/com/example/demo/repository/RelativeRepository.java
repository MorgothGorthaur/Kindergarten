package com.example.demo.repository;

import com.example.demo.model.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RelativeRepository extends JpaRepository<Relative, Long> {
    @Query(value = "select relatives.* from relatives\n" +
            "join children_relatives cr on relatives.id = cr.relatives_id\n" +
            "join children c on c.id = cr.kids_id\n" +
            "join teachers t on c.group_id = t.group_id\n" +
            "where relatives_id = ?1 and c.id = ?2 and t.email = ?3", nativeQuery = true)
    Optional<Relative> getRelative(long relativeId, long kidsId, String email);
}
