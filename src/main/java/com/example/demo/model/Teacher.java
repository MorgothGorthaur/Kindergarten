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
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class Teacher extends Account {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "phone_number", nullable = false)
    private String phone;
    @Column(name = "skype")
    private String skype;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Group group;

    public Teacher(String name, String phone, String skype, String email, String password) {
        super(email, password, Role.ROLE_USER);
        this.name = name;
        this.phone = phone;
        this.skype = skype;
    }

    public void addGroup(Group group) {
        if (this.group != null) throw new TeacherAlreadyContainsGroupException(getEmail());
        this.group = group;
        group.setTeacher(this);
    }
}