package com.mymoneyapp.backend.config;

import com.mymoneyapp.backend.config.filter.AuthenticationFilter;
import com.mymoneyapp.backend.config.handler.LoginSuccessHandler;
import com.mymoneyapp.backend.config.handler.LogoutSuccessHandler;
import com.mymoneyapp.backend.config.provider.LoginAuthenticationProvider;
import com.mymoneyapp.backend.config.provider.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StreamUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginAuthenticationProvider loginAuthenticationProvider;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Value("${cors-path}")
    private String path;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**", "/").permitAll()
                    .antMatchers("/login", "/register").permitAll()
                .and()
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler((request, response, exception) -> {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    })
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .logoutSuccessHandler(new LogoutSuccessHandler())
                    .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    })
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                .csrf()
                    .disable()
                .cors()
                .and()
                .anonymous()
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addExposedHeader("Authorization");

        source.registerCorsConfiguration(path, corsConfiguration);

        return source;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthenticationProvider)
                .authenticationProvider(tokenAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RsaSigner rsaSigner(final ResourceLoader resourceLoader, @Value("${ssh-key.private}") final String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new RsaSigner(
                StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8")));
    }

    @Bean
    public RsaVerifier rsaVerifier(final ResourceLoader resourceLoader, @Value("${ssh-key.public}") final String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new RsaVerifier(
                StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8")));
    }

}
