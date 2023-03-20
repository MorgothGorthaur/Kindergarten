package com.example.demo.controller.security;

import com.example.demo.model.TeacherUserDetails;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface TokenProvider {
    Map<String, String> generateTokens(String requestUrl, TeacherUserDetails user);

    void verifyTokens(String accessToken);

    Map<String, String> verifyAndRegenerateAccessToken(String refreshToken, String requestUrl);
}
