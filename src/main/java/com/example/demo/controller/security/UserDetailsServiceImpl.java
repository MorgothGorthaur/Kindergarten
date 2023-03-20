package com.example.demo.controller.security;

import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.TeacherUserDetails;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final TeacherRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new TeacherUserDetails(repository.findTeacherByEmail(username)
                .orElseThrow(() -> new TeacherNotFoundException(username)));
    }
}
