package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuthUserService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public User loadUserByUsername(final String email) {
        log.info("C=OAuthUserService, M=loadUserByUsername, T=Email {}", email);

        return userService.retrieve(email);
    }

}
