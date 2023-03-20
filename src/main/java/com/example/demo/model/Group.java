package com.example.demo.model;

import com.example.demo.exception.ToBigChildrenInGroupException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter @Setter
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
    private Set<Child> kids;

    @OneToOne(mappedBy = "group")
    private Teacher teacher;

    public Group(String name, int maxSize) {
        this.name = name;
        this.maxSize = maxSize;
    }

    public void addChild(Child child) {
        if(this.kids == null) kids = new HashSet<>();
        if(kids.size() == maxSize) throw new ToBigChildrenInGroupException(maxSize);
        kids.add(child);
        child.setGroup(this);
    }

    public int getCurrentSize() {
        return kids != null ? kids.size() : 0;
    }
}