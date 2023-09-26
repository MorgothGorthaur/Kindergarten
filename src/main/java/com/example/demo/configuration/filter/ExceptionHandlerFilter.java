package com.example.demo.configuration.filter;

import com.example.demo.dto.ApiError;
import com.example.demo.exception.BadPasswordOrEmailException;
import com.example.demo.exception.BadTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Order(1)
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BadPasswordOrEmailException | BadTokenException ex) {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, ex.getMessage());
            response.setStatus(FORBIDDEN.value());
            List<String> debugMessages = Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).stream().toList();
            ApiError error = new ApiError(ex.getMessage(), debugMessages);
            response.setContentType(APPLICATION_JSON_VALUE);
            mapper.writeValue(response.getOutputStream(), error);
        }
    }
}
