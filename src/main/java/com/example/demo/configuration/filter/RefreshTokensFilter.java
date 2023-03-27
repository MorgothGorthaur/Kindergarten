package com.example.demo.configuration.filter;

import com.example.demo.controller.security.TokenProvider;
import com.example.demo.enums.AuthorizationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class RefreshTokensFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final String refreshUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(AuthorizationType.BEARER.getPrefix()) && request.getServletPath().equals(refreshUrl))
            regenerateToken(request, response, authorizationHeader);
        else filterChain.doFilter(request, response);
    }

    private void regenerateToken(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException {
        var token = tokenProvider.regenerateAccessTokenIfRefreshTokenIsValid(
                authorizationHeader.substring(AuthorizationType.BEARER.getPrefix().length()),
                request.getRequestURL().toString());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }
}
