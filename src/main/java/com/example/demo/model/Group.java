package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_size", nullable = false)
    private int maxSize;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Child> kids = new HashSet<>();

    @OneToOne(mappedBy = "group")
    private Teacher teacher;

    public Group(String name, int maxSize) {
        this.name = name;
        this.maxSize = maxSize;
    }

    public int getCurrentSize() {
        return kids.size();
    }
}