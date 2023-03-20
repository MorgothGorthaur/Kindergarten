package com.example.demo.controller.security;

import com.example.demo.model.TeacherUserDetails;

import java.util.Map;

public interface TokenProvider {
    Map<String, String> generateTokens(String requestUrl, TeacherUserDetails user);

    void verifyAccessToken(String accessToken);

    Map<String, String> verifyRefreshAndRegenerateAccessToken(String refreshToken, String requestUrl);
}
