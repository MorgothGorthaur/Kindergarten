package com.example.demo.service;

import com.example.demo.model.Group;

public interface GroupService {

    /**
     * adds group to teacher and saves it to db.
     */
    void save(String email, Group group);
}
