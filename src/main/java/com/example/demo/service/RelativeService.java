package com.example.demo.service;

import com.example.demo.exception.kindergarten.notfound.ChildNotFoundException;
import com.example.demo.exception.kindergarten.notfound.RelativeNotFoundException;
import com.example.demo.model.Relative;

public interface RelativeService {

    /**
     * if a relative with the same fields isn`t found in the db,
     * adds it to the child. else adds to the child a relative from the db.
     *
     * @param email   the ID of the teacher related with the relative
     * @param childId the ID of the relative`s child
     * @param name    the relative`s name
     * @param address the relative`s address
     * @param phone   the relative`s phone number
     * @throws ChildNotFoundException if the child with the given ID and teacher`s email is not found
     */
    Relative save(String email, long childId, String name, String address, String phone);

    /**
     * removes the relative from the child and saves changes to the db.
     *
     * @param email      the ID of the teacher related with the relative
     * @param childId    the ID of the relative`s child
     * @param relativeId the relative`s ID
     * @throws RelativeNotFoundException if the relative with the given email, relativeId and childId is not found
     * @throws ChildNotFoundException    if the child related to the relative is not found
     */

    void delete(String email, long childId, long relativeId);

    /**
     * If a relative with the same fields isn`t found in the db,
     * adds the relative to the child. Otherwise, it replaces the updated relative with the relative that has the same fields.
     *
     * @param email   the ID of the teacher related with the relative
     * @param childId the ID of the relative`s child
     * @param name    the new name for the relative
     * @param address the new address for the relative
     * @param phone   the new phone number for the relative
     * @throws RelativeNotFoundException if the relative with the given email, relativeId and childId is not found
     * @throws ChildNotFoundException    if the child with the given ID and teacher`s email is not found
     */
    Relative updateOrReplaceRelative(String email, long childId, long relativeId, String name, String address, String phone);
}
