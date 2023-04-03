package com.example.demo.controller.security;

import com.example.demo.enums.Role;
import com.example.demo.enums.Token;
import com.example.demo.exception.BadTokenException;
import com.example.demo.model.Teacher;
import com.example.demo.model.UserDetails;
import com.example.demo.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProviderImpl tokenProvider;

    @MockBean
    private TeacherRepository teacherRepository;


    @Test
    void testGenerateTokens() {
        var teacher = new Teacher("John", "1234567", "john_skype", "john@example.com", "password");
        teacher.setId(0L);
        teacher.setRole(Role.ROLE_USER);
        when(teacherRepository.findTeacherByEmail(anyString())).thenReturn(Optional.of(teacher));
        var userDetails = new UserDetails(teacher);
        var tokens = tokenProvider.generateTokens("/login", userDetails);
        assertThat(tokens).isNotNull();
        assertThat(tokens.get(Token.ACCESS_TOKEN.getTokenType())).isNotNull();
        assertThat(tokens.get(Token.REFRESH_TOKEN.getTokenType())).isNotNull();

    }

    @Test
    void testAuthorizeIfAccessTokenIsValid() {
        var teacher = new Teacher("John", "1234567", "john_skype", "john@example.com", "password");
        teacher.setId(0L);
        teacher.setRole(Role.ROLE_USER);
        var userDetails = new UserDetails(teacher);
        var tokens = tokenProvider.generateTokens("/login", userDetails);

        tokenProvider.authorizeIfAccessTokenIsValid(tokens.get(Token.ACCESS_TOKEN.getTokenType()));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(userDetails.getUsername());
        assertThat(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray())
                .containsExactlyInAnyOrder(Role.ROLE_USER.toString());
    }

    @Test
    void testAuthorizeIfAccessTokenIsValid_shouldThrowBadTokenException_withInvalidToken() {
        var invalidToken = "invalid-token";
        assertThrows(BadTokenException.class, () -> tokenProvider.authorizeIfAccessTokenIsValid(invalidToken));
    }

    @Test
    void testAuthorizeIfAccessTokenIsValid_shouldThrowBadTokenException_withExpiredToken() {
        var expiredToken = """
                eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.
                eyJzdWIiOiJ2YXN5YXB1cGtpbkBnbWFpbC5jb20iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG
                9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNjc4NDk1NTc5fQ.BOuGXju7RUGvP2qfoA6ZRNHk6kV7IuSlENm4qPkXQ8Q""";
        assertThrows(BadTokenException.class, () -> tokenProvider.authorizeIfAccessTokenIsValid(expiredToken));
    }


    @Test
    void testRegenerateAccessTokenIfRefreshTokenIsValid() {
        var teacher = new Teacher("John", "1234567", "john_skype", "john@example.com", "password");
        teacher.setId(0L);
        teacher.setRole(Role.ROLE_USER);
        var userDetails = new UserDetails(teacher);
        when(teacherRepository.findTeacherByEmail(anyString())).thenReturn(Optional.of(teacher));
        var tokens = tokenProvider.generateTokens("/login", userDetails);
        var result = tokenProvider.regenerateAccessTokenIfRefreshTokenIsValid(tokens.get(Token.REFRESH_TOKEN.getTokenType()), "/refresh");
        assertThat(result).isNotNull();
    }

    @Test
    void testRegenerateAccessTokenIfRefreshTokenIsValid_shouldThrowBadTokenException_withInvalidToken() {
        var invalidToken = "invalid-token";
        assertThrows(BadTokenException.class, () -> tokenProvider.regenerateAccessTokenIfRefreshTokenIsValid(invalidToken, "/refresh"));
    }

    @Test
    void testRegenerateAccessTokenIfRefreshTokenIsValid_shouldThrowBadTokenException_withExpiredToken() {
        var expiredToken = """
                eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
                .eyJzdWIiOiJ2aWN0b3JAZ21haWwuY29tIiwiaXNzIjoiaHR0cDovL2xvY2FsaG
                9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNjc5NDIyNTQwfQ.2ddMsRQPlfPCurDi749DFIqcEIhuKqRvyhdXg8DW3Iw""";
        assertThrows(BadTokenException.class, () -> tokenProvider.regenerateAccessTokenIfRefreshTokenIsValid(expiredToken, "/refresh"));
    }

}