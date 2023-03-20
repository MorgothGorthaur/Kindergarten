package com.example.demo.service;

import com.example.demo.model.Relative;

public interface RelativeService {

    /**
     * if relative with the same fields isn`t found in db, adds it to child. else adds to child relative from db.
     */
    Relative add(String email, long childId, String name, String address, String phone);

    /**
     * removes relative from child and saves changes to db.
     */

    void delete(String email, long childId, long relativeId);

    /**
     * if relatives with the same fields isn`t found in db, updates relative by id. else replaces relative by id with relative with same fields
     */
    void updateOrReplaceRelative(String email, long childId, long relativeId, String name, String address, String phone);
}
