package com.example.demo.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "children")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "birth_year", nullable = false)
    private LocalDate birthYear;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Relative> relatives = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Child(String name, LocalDate birthYear, Relative relative) {
        this.name = name;
        this.birthYear = birthYear;
        addRelative(relative);
    }

    public Child(String name, LocalDate birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public void addRelative(Relative relative) {
        relatives.add(relative);
    }

    public void removeRelative(Relative relative) {
        relatives.remove(relative);
    }


}