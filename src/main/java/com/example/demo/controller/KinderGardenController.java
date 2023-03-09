package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.exception.BadTokenException;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TokensGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/kindergarten")
@RequiredArgsConstructor
public class KinderGardenController {
    private final TokensGenerator tokensGenerator;
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    private final TeacherRepository teacherRepository;

    @GetMapping("/refresh")
    public void refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) regenerateToken(request, response, authorizationHeader);
        else throw new BadTokenException();
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
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (Exception ex) {
            throw new BadTokenException();
        }
    }
}
