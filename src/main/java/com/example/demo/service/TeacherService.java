package com.example.demo.service;

import com.example.demo.exception.kindergarten.notacceptable.AccountAlreadyExistException;
import com.example.demo.model.Teacher;

public interface TeacherService {

    /**
     * saves the teacher to the database
     *
     * @param teacher the teacher to be saved to the db
     * @throws AccountAlreadyExistException if a teacher with the same email already exists in the database
     */
    void save(Teacher teacher);


    void update(String oldEmail, String newEmail, String newPassword, String newName, String newSkype, String newPhone);

    void delete(String email);
}
