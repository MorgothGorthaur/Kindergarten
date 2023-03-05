package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "relatives")
@Getter @Setter
public class Relative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "age", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;
    @ManyToMany(mappedBy = "relatives", fetch = FetchType.LAZY)
    private Set<Child> kids;

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Relative relative)) return false;
        return id.equals(relative.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}