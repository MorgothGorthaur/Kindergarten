package com.example.demo.service;

import com.example.demo.model.Relative;

public interface RelativeService {
    Relative add(String email, long childId, Relative relative);

    void delete(String email, long childId, long relativeId);

    void updateOrReplaceRelative(String email, long childId, long relativeId, Relative relative);
}
