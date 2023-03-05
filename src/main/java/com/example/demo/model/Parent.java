package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "parent")
@Getter @Setter
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "age", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phoneNumber;
    @OneToOne(mappedBy = "parent", fetch = FetchType.LAZY)
    private Child child;
}