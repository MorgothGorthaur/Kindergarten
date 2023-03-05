package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.model.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
@Component
public class TokensGeneratorImpl implements TokensGenerator {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.access_token.time}")
    private Integer ACCESS_TOKEN_TIME;

    @Value("${jwt.refresh_token.time}")
    private Integer REFRESH_TOKEN_TIME;

    @Override
    public Map<String, String> generateTokens(HttpServletRequest request, UserDetailsImpl user) {
        var instant = Instant.now();
        var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        var access = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(ACCESS_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList()).sign(algorithm);
        var refresh = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(REFRESH_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString()).sign(algorithm);
        var tokens = new HashMap<String, String>();
        tokens.put("access_token", access);
        tokens.put("refresh_token", refresh);
        return tokens;
    }
}
