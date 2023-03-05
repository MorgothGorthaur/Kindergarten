package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.TeacherDto;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.Actuality;
import com.example.demo.model.Teacher;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TokensGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/kindergarden")
@RequiredArgsConstructor
public class KinderGardenController {
    private final TokensGenerator tokensGenerator;
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private final PasswordEncoder encoder;
    private final TeacherRepository teacherRepository;

    @PostMapping
    public void addTeacher(@RequestBody TeacherDto dto){
        var teacher = dto.toTeacherDto();
        teacher.setPassword(encoder.encode(teacher.getPassword()));
        teacherRepository.save(teacher);
    }

    @GetMapping("/refresh")
    public void refreshTokens(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            var refresh_token = authorizationHeader.substring("Bearer ".length());
            var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(refresh_token);
            var username = decoderJWT.getSubject();
            var user = new UserDetailsImpl(teacherRepository.findTeacherByEmailAndActuality(username, Actuality.ACTIVE)
                    .orElseThrow(() -> new TeacherNotFoundException(username)));
            var tokens = tokensGenerator.generateTokens(request, user);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
