package com.example.demo.configuration.filter;

import com.example.demo.controller.security.TokenProvider;
import com.example.demo.enums.AuthorizationType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
@Order(3)
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (containsBearerToken(authorizationHeader))
            tokenProvider.authorizeIfAccessTokenIsValid(authorizationHeader.substring(AuthorizationType.BEARER.getPrefix().length()));
        filterChain.doFilter(request, response);
    }

    private boolean containsBearerToken(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(AuthorizationType.BEARER.getPrefix());
    }

}
