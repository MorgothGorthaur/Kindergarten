package com.example.demo.configuration.filter;


import com.example.demo.controller.security.TokenProvider;
import com.example.demo.enums.AuthenticationRequestParameter;
import com.example.demo.exception.BadPasswordOrEmailException;
import com.example.demo.model.AccountDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var email = request.getParameter(AuthenticationRequestParameter.EMAIL.getParameter());
        var password = request.getParameter(AuthenticationRequestParameter.PASSWORD.getParameter());
        var token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticate(token);
    }

    private Authentication authenticate(UsernamePasswordAuthenticationToken token) {
        try {
            return authenticationManager.authenticate(token);
        } catch (InternalAuthenticationServiceException | BadCredentialsException ex) {
            throw new BadPasswordOrEmailException(token.getName());
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        var user = (AccountDetails) authResult.getPrincipal();
        var tokens = tokenProvider.generateTokens(request.getRequestURL().toString(), user);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
