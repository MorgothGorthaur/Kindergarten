package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "relatives")
@Getter @Setter
@NoArgsConstructor
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

    public Relative(String name, String phone, String address) {
        this.name = name;
        this.phoneNumber = phone;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Relative relative)) return false;
        return Objects.equals(id, relative.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void addChild(Child child) {
        if(kids == null) kids = new HashSet<>();
        kids.add(child);
    }
}