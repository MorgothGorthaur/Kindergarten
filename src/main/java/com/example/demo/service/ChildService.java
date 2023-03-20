package com.example.demo.service;

import com.example.demo.model.Child;

public interface ChildService {

    /**
     * adds child to group and saves it to db.
     *
     * @param email
     * @param child
     * @return child
     */
    Child add(String email, Child child);
}
