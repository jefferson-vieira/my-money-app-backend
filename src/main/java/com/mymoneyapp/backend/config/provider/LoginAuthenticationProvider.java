package com.mymoneyapp.backend.config.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymoneyapp.backend.exception.UserNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RsaSigner rsaSigner;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @SneakyThrows
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        UserDetails ud = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

        if (!passwordEncoder.matches((CharSequence) authentication.getCredentials(), ud.getPassword())) {
            throw new UserNotFoundException();
        }

        Jwt jwt = JwtHelper.encode(objectMapper.writeValueAsString(ud), rsaSigner);

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities());
        user.setDetails(jwt.getEncoded());

        return user;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
