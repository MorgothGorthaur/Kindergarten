package com.example.demo.controller.security;

import com.example.demo.model.TeacherUserDetails;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface TokenProvider {
    Map<String, String> generateTokens(HttpServletRequest request, TeacherUserDetails user);

    void verifyTokens(String authorizationHeader);

    Map<String, String> verifyAndRegenerateAccessToken(String authorizationHeader, HttpServletRequest request);
}
