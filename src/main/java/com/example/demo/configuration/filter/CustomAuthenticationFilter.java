package com.example.demo.configuration.filter;

import com.example.demo.enums.ContentType;
import com.example.demo.enums.RequestParameter;
import com.example.demo.exception.BadPasswordOrEmailException;
import com.example.demo.model.TeacherUserDetails;
import com.example.demo.controller.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
   private final AuthenticationManager authenticationManager;
   private final TokenProvider tokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
            var email = request.getParameter(RequestParameter.EMAIL.getParameter());
            var password = request.getParameter(RequestParameter.PASSWORD.getParameter());
            var token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticate(email, password, token);
    }

    private Authentication authenticate(String email, String password, UsernamePasswordAuthenticationToken token) {
        try {
            return authenticationManager.authenticate(token);
        } catch (Exception ex) {
            throw new BadPasswordOrEmailException(email, password);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException{
        var user = (TeacherUserDetails) authResult.getPrincipal();
        var tokens = tokenProvider.generateTokens(request, user);
        response.setContentType(ContentType.JSON.getContentType());
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
