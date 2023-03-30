package com.example.demo.service;

import com.example.demo.model.Teacher;

public interface TeacherService {

    /**
     * saves the teacher to the database
     *
     * @param teacher the teacher to be saved to the db
     * @throws com.example.demo.exception.TeacherAlreadyExistException if a teacher with the same email already exists in the database
     */
    void save(Teacher teacher);
}
