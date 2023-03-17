package com.example.demo.configuration.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.enums.AuthorizationType;
import com.example.demo.enums.Token;
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
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
@RequiredArgsConstructor
public class RefreshTokensFilter extends OncePerRequestFilter {
    private final TokensGenerator tokensGenerator;
    private final TeacherRepository teacherRepository;
    private final String secretKey;

    private final String refreshUrl;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(AuthorizationType.BEARER.getPrefix()) && request.getServletPath().equals(refreshUrl)) regenerateToken(request, response, authorizationHeader);
        else filterChain.doFilter(request,response);
    }
    private void regenerateToken(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) {
        try {
            var refresh_token = authorizationHeader.substring(AuthorizationType.BEARER.getPrefix().length());
            var algorithm = Algorithm.HMAC256(secretKey.getBytes());
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(refresh_token);
            var username = decoderJWT.getSubject();
            var user = new UserDetailsImpl(teacherRepository.findTeacherByEmail(username)
                    .orElseThrow(() -> new TeacherNotFoundException(username)));
            var tokens = tokensGenerator.generateTokens(request, user);
            tokens.remove(Token.REFRESH_TOKEN.getTokenType());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (Exception ex) {
            throw new BadTokenException();
        }
    }
}
