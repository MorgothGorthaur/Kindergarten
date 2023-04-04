package com.example.demo.controller.security;

import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.AccountDetails;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository repository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AccountDetails(repository.findUserByEmail(username)
                .orElseThrow(() -> new TeacherNotFoundException(username)));
    }
}