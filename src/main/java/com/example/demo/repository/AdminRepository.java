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
    @Query(value = """
              UPDATE admins ad
              JOIN accounts a ON ad.account_id = a.id
              SET ad.phone_number = ?2
              WHERE a.email = ?1
            """, nativeQuery = true)
    int updateAdminByEmail(String oldEmail, String newPhone);

    @Modifying
    @Transactional
    @Query("DELETE FROM Admin a WHERE a.email = ?1")
    int deleteAdminByEmail(String email);
}
