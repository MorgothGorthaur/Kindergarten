package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.enums.AuthorizationType;
import com.example.demo.enums.Claim;
import com.example.demo.enums.Token;
import com.example.demo.exception.BadTokenException;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class TokensServiceImpl implements TokensService {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.access_token.time}")
    private long ACCESS_TOKEN_TIME;

    @Value("${jwt.refresh_token.time}")
    private long REFRESH_TOKEN_TIME;

    private final TeacherRepository repository;
    @Override
    public Map<String, String> generateTokens(HttpServletRequest request, UserDetailsImpl user) {
        var tokens = new HashMap<String, String>();
        tokens.put(Token.ACCESS_TOKEN.getTokenType(), generateAccessToken(request, user));
        tokens.put(Token.REFRESH_TOKEN.getTokenType(), generateRefreshToken(request, user));
        return tokens;
    }

    @Override
    public void verifyTokens(String authorizationHeader) {
        try {
            var accessToken = authorizationHeader.substring(AuthorizationType.BEARER.getPrefix().length());
            var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(accessToken);
            var username = decoderJWT.getSubject();
            var roles = decoderJWT.getClaim(Claim.ROLES.getClaim()).asArray(String.class);
            var authorities = new ArrayList<SimpleGrantedAuthority>();
            stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception ex) {
            throw new BadTokenException();
        }
    }

    @Override
    public Map<String, String> verifyAndRegenerateAccessToken(String authorizationHeader, HttpServletRequest request) {
        try {
            var refreshToken = authorizationHeader.substring(AuthorizationType.BEARER.getPrefix().length());
            var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(refreshToken);
            var username = decoderJWT.getSubject();
            var user = new UserDetailsImpl(repository.findTeacherByEmail(username)
                    .orElseThrow(() -> new TeacherNotFoundException(username)));
            var tokens = new HashMap<String, String>();
            tokens.put(Token.ACCESS_TOKEN.getTokenType(), generateAccessToken(request, user));
            return tokens;
        } catch (Exception ex) {
            throw new BadTokenException();
        }
    }

    private String generateRefreshToken(HttpServletRequest request, UserDetailsImpl user) {
        var instant = Instant.now();
        var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(REFRESH_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString()).sign(algorithm);
    }

    private String generateAccessToken(HttpServletRequest request, UserDetailsImpl user) {
        var instant = Instant.now();
        var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(ACCESS_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(Claim.ROLES.getClaim(), user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList()).sign(algorithm);
    }
}
