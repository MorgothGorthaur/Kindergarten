package com.example.demo.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.model.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Map;

public interface TokensGenerator {
    Map<String, String> generateTokens(HttpServletRequest request, UserDetailsImpl user);
}
