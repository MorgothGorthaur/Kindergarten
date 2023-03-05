package com.example.demo.configuration.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.exception.BadTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final String LOGIN_URL;
    private final String REFRESH_URL;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(LOGIN_URL) || request.getServletPath().equals(REFRESH_URL)) {
            filterChain.doFilter(request, response);
        } else {
            var authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                verifyTokens(request, response, filterChain, authorizationHeader);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    private void verifyTokens(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String authorizationHeader) throws IOException {
        try {
            var token = authorizationHeader.substring("Bearer ".length());
            var algorithm = Algorithm.HMAC256("secret".getBytes());
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(token);
            var username = decoderJWT.getSubject();
            var roles = decoderJWT.getClaim("roles").asArray(String.class);
            var authorities = new ArrayList<SimpleGrantedAuthority>();
            stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            throw new BadTokenException(ex.getMessage());
        }
    }
}
