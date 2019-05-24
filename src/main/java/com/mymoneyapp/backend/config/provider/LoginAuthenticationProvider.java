package com.mymoneyapp.backend.config.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymoneyapp.backend.exception.UserNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
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
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

        checkIfAccountCanBeAuthenticated(userDetails);

        if (!passwordEncoder.matches((CharSequence) authentication.getCredentials(), userDetails.getPassword())) {
            throw new UserNotFoundException();
        }

        Jwt jwt = JwtHelper.encode(objectMapper.writeValueAsString(userDetails), rsaSigner);

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        user.setDetails(jwt.getEncoded());

        return user;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void checkIfAccountCanBeAuthenticated(UserDetails userDetails) {

        if (!userDetails.isEnabled()) {
            throw new DisabledException("Essa conta foi desativada");
        }

        if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("Essa conta ainda não foi ativada");
        }

        if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("Essa conta expirou");
        }

        if (!userDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("As permissões dessa conta estão expiradas");
        }

    }
}
