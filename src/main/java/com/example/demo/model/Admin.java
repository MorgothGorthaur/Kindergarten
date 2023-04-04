package com.example.demo.model;

import com.example.demo.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class Admin extends Account {
    @Column(name = "phone_number", nullable = false)
    private String phone;

    public Admin(String email, String password, String phone) {
        super(email, password, Role.ROLE_ADMIN);
        this.phone = phone;
    }
}
