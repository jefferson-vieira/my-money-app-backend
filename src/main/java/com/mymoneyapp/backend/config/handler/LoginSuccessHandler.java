package com.mymoneyapp.backend.config.handler;

import com.mymoneyapp.backend.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
        log.info("C=LoginSuccessHandler, M=onAuthenticationSuccess, T=User {}", ((User) authentication.getPrincipal()).getUsername());

        response.setHeader("Authorization", authentication.getDetails().toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
