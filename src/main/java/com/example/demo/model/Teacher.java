package com.example.demo.model;

import com.example.demo.exception.GroupContainsKidsException;
import com.example.demo.exception.TeacherAlreadyContainsGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@Getter @Setter
@NoArgsConstructor
public class Teacher {
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

    @Column(name = "role", nullable = false)
    private Role role;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Group group;

    public Teacher(String name, String phone, String skype, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.skype = skype;
        this.email = email;
        this.password = password;
        this.role = Role.ROLE_USER;
    }

    public void removeGroup() {
        if(group != null) {
            if(group.getKids() != null && group.getKids().size() != 0) throw new GroupContainsKidsException();
            group.setTeacher(null);
        }
        group = null;
    }

    public void addGroup(Group group) {
        if(this.group != null) throw new TeacherAlreadyContainsGroup(email);
        this.group = group;
        group.setTeacher(this);
    }
}