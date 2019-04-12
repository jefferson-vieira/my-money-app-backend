package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.UserRequest;
import com.mymoneyapp.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping
public class OAuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @ApiOperation(value = "Retorna o usuário logado", authorizations = @Authorization("OAuth"))
    public User principal(@AuthenticationPrincipal final User user) {
        return user;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Cadastra um usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@Valid @RequestBody final UserRequest userRequest) {
        Long userId = userService.save(userRequest);
        return ResponseEntity.created(URI.create("/users/" + userId)).build();
    }

}
