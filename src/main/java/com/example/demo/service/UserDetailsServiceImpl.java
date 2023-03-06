package com.example.demo.service;

import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.model.Actuality;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final TeacherRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(repository.findTeacherByEmailAndActuality(username, Actuality.ACTIVE)
                .orElseThrow(() -> new TeacherNotFoundException(username)));
    }
}
