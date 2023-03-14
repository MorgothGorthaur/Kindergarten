package com.example.demo.configuration.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.exception.BadTokenException;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TokensGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
@RequiredArgsConstructor
public class RefreshTokensFilter extends OncePerRequestFilter {
    private final TokensGenerator tokensGenerator;
    private final String SECRET_KEY;

    private final String REFRESH_URL;
    private final TeacherRepository teacherRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && request.getServletPath().equals(REFRESH_URL)) regenerateToken(request, response, authorizationHeader);
        else filterChain.doFilter(request,response);
    }
    private void regenerateToken(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) {
        try {
            var refresh_token = authorizationHeader.substring("Bearer ".length());
            var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(refresh_token);
            var username = decoderJWT.getSubject();
            var user = new UserDetailsImpl(teacherRepository.findTeacherByEmail(username)
                    .orElseThrow(() -> new TeacherNotFoundException(username)));
            var tokens = tokensGenerator.generateTokens(request, user);
            tokens.remove("refresh_token");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (Exception ex) {
            throw new BadTokenException();
        }
    }
}
