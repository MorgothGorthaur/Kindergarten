package com.example.demo.controller.security;

import com.example.demo.model.TeacherUserDetails;

import java.util.Map;

public interface TokenProvider {
    Map<String, String> generateTokens(String requestUrl, TeacherUserDetails user);

    /**
     * verifies access token, next creates a UsernamePasswordAuthenticationToken and adds it to a SecurityContextHolder
     *
     * @param accessToken the access token to be verified and used for authorization.
     * @throws com.example.demo.exception.BadTokenException if the access token is expired or invalid
     */
    void authorizeIfAccessTokenIsValid(String accessToken);

    /**
     * @param refreshToken the refresh token to be verified and used to generate a new access token.
     * @param requestUrl   the request URL for generating a new access token.
     * @return a map with a new access token.
     * @throws com.example.demo.exception.BadTokenException if the refresh token is expired or invalid,
     *                                                      or if the teacher associated with the refresh token is not found in the repository.
     */

    Map<String, String> regenerateAccessTokenIfRefreshTokenIsValid(String refreshToken, String requestUrl);
}
