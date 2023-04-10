package com.example.demo.service;

import com.example.demo.model.Child;

import java.time.LocalDate;

public interface ChildService {

    /**
     * adds the child to the group and saves it to the db.
     *
     * @param email the teacher`s email
     * @param child the child to be added to the teacher`s group and saved to the db
     * @throws com.example.demo.exception.GroupNotFoundException if no group is found for the teacher with the given email
     */
    Child save(String email, Child child);


    void update(String email, long id, String name, LocalDate birthYear);

    void delete(long id, String email);
}
