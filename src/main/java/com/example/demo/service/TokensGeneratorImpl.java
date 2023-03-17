package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.configuration.filter.enums.Keys;
import com.example.demo.configuration.filter.enums.TokenTime;
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

    @Override
    public Map<String, String> generateTokens(HttpServletRequest request, UserDetailsImpl user) {
        var instant = Instant.now();
        var algorithm = Algorithm.HMAC256(Keys.SECRET_KEY.getValue().getBytes());
        var access = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(TokenTime.ACCESS_TOKEN.getTime(), ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList()).sign(algorithm);
        var refresh = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(TokenTime.REFRESH_TOKEN.getTime(), ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString()).sign(algorithm);
        var tokens = new HashMap<String, String>();
        tokens.put("access_token", access);
        tokens.put("refresh_token", refresh);
        return tokens;
    }
}
