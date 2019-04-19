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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/validate/{encodedEmail}")
    @ApiOperation(value = "Valída o e-mail do usuário cadastrado",  authorizations = @Authorization("OAuth"))
    public void validateUserEmail (@PathVariable final String encodedEmail) {
        userService.validationUserEmail(encodedEmail);
    }

}
