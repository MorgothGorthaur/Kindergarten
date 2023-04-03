package com.example.demo.model;

import com.example.demo.enums.Role;
import com.example.demo.exception.TeacherAlreadyContainsGroupException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "phone_number", nullable = false)
    private String phone;
    @Column(name = "skype")
    private String skype;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Group group;

    public Teacher(String name, String phone, String skype, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.skype = skype;
        this.setEmail(email);
        this.setPassword(password);
        this.setRole(Role.ROLE_USER);
    }

    public void addGroup(Group group) {
        if (this.group != null) throw new TeacherAlreadyContainsGroupException(getEmail());
        this.group = group;
        group.setTeacher(this);
    }
}