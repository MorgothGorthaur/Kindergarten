package com.example.demo.repository;

import com.example.demo.model.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RelativeRepository extends JpaRepository<Relative, Long> {

    /**
     * finds a relative with a child and all of its relatives by the relative`s id, the child`s id and the teacher`s email
     *
     * @param relativeId the relative`s ID
     * @param kidsId     the kid`s ID
     * @param email      the teacher`s email
     * @return Optional of the relative or empty optional if relative is not found
     */
    @Query("SELECT r FROM Relative r JOIN FETCH r.kids k JOIN FETCH k.relatives WHERE r.id = ?1 AND k.id = ?2 AND k.group.teacher.email = ?3")
    Optional<Relative> findRelativeWithChild(long relativeId, long kidsId, String email);

    Optional<Relative> findRelativeByNameAndPhoneAndAddress(String name, String phone, String address);

    Optional<Relative> findRelativeByNameAndPhoneAndAddressAndIdNot(String name, String phone, String address, long id);

    @Query("SELECT r FROM Relative r JOIN FETCH r.kids k JOIN FETCH k.group.teacher")
    List<Relative> findAllRelativesWithKidsAndTeachers();

    @Query("SELECT r FROM Relative r JOIN FETCH r.kids k JOIN FETCH k.group.teacher ORDER BY (r.name)")
    List<Relative> findAllRelativesWithKidsAndTeachersOrderByName();

    @Query("SELECT r FROM Relative r JOIN FETCH r.kids k JOIN FETCH k.group.teacher ORDER BY (r.address)")
    List<Relative> findAllRelativesWithKidsAndTeachersOrderByAddress();

    @Query("SELECT r FROM Relative r JOIN FETCH r.kids k JOIN FETCH k.group.teacher ORDER BY SIZE(r.kids) DESC ")
    List<Relative> findAllRelativesWithKidsAndTeachersOrderByKidCount();

}
