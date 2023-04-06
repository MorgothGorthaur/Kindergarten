package com.example.demo.service;

import com.example.demo.model.Admin;

public interface AdminService {
    void save(Admin admin);


    void update(String oldEmail, String newEmail, String newPassword, String newPhone);
}
