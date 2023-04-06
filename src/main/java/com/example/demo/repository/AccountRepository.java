package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findUserByEmail(String email);


    @Modifying
    @Query(value = """
            UPDATE accounts a SET a.email = ?2, a.password = ?3 WHERE a.email = ?1
            AND NOT EXISTS (SELECT a2.id FROM accounts a2 WHERE a2.email = ?2 AND a2.id <> a.id)
            """, nativeQuery = true)
    int updateAccountByEmail(String oldEmail, String newEmail, String newPassword);

}
