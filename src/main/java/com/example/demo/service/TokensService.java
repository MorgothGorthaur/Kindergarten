package com.example.demo.service;

import com.example.demo.model.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface TokensService {
    Map<String, String> generateTokens(HttpServletRequest request, UserDetailsImpl user);

    void verifyTokens(String authorizationHeader);

    Map<String, String> verifyAndRegenerateAccessToken(String authorizationHeader, HttpServletRequest request);
}
