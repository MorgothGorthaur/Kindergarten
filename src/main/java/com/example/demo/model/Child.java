package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "child")
@Getter
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "age", nullable = false)
    private int age;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Parent parent;
}