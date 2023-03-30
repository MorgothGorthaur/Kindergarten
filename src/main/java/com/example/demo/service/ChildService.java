package com.example.demo.service;

import com.example.demo.model.Child;

public interface ChildService {

    /**
     * adds child to group and saves it to db.
     */
    Child save(String email, Child child);
}
