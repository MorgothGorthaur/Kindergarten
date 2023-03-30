package com.example.demo.service;

import com.example.demo.model.Group;

public interface GroupService {

    /**
     * adds the group to the teacher and saves it to the database.
     *
     * @param email the teacher`s email
     * @param group the group to be added to the teacher
     * @throws com.example.demo.exception.TeacherNotFoundException if the teacher with the given email is not found
     */
    void save(String email, Group group);
}
