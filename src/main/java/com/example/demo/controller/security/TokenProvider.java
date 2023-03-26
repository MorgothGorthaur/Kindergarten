package com.example.demo.controller.security;

import com.example.demo.model.TeacherUserDetails;

import java.util.Map;

public interface TokenProvider {
    Map<String, String> generateTokens(String requestUrl, TeacherUserDetails user);

    /**
     * verifies access token, next creates a UsernamePasswordAuthenticationToken and adds it to a SecurityContextHolder
     */
    void authorizeIfAccessTokenIsValid(String accessToken);

    Map<String, String> regenerateAccessTokenIfRefreshTokenIsValid(String refreshToken, String requestUrl);
}
