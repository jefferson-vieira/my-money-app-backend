package com.mymoneyapp.backend.config.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymoneyapp.backend.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RsaVerifier rsaVerifier;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        try {
            Jwt jwt = JwtHelper.decodeAndVerify((String) authentication.getPrincipal(), rsaVerifier);
            User user = objectMapper.readValue(jwt.getClaims(), User.class);
            return new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        } catch (Exception e) {
            throw new PreAuthenticatedCredentialsNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }

}
