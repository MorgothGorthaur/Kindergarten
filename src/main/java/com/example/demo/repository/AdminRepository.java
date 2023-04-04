package com.example.demo.repository;

import com.example.demo.model.Admin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findAdminByEmail(String email);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Admin a SET a.email = ?2, a.password = ?3, a.phone = ?4
            WHERE a.email = ?1 AND NOT EXISTS (SELECT a2 FROM Admin a2 WHERE a2.email = ?2 AND a2.id <> a.id)""")
    int updateAdminByEmail(String oldEmail, String newEmail, String newPassword, String newPhone);

    @Modifying
    @Transactional
    @Query("DELETE FROM Admin a WHERE a.email = ?1")
    int deleteAdminByEmail(String email);
}
