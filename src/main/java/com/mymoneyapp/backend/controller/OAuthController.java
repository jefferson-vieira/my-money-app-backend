package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.UserChangePassRequest;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping
public class OAuthController {

    @Autowired
    private UserService userService;

    @GetMapping ("/me")
    @ApiOperation(value = "Retorna o usuário logado", authorizations = @Authorization("OAuth"))
    public User principal(@AuthenticationPrincipal final User user) {
        return user;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Cadastra um usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@Valid @RequestBody final UserRequest userRequest) {
        //falta um exception caso o email ja tenha sido usado
        //E nao mostrar a senha no log, tanto para registrar, quanto para alterar a senha
        Long userId = userService.save(userRequest);
        return ResponseEntity.created(URI.create("/users/" + userId)).build();
    }

    @GetMapping("/registration-confirm/{token}")
    @ApiOperation(value = "Valída o e-mail do usuário cadastrado",  authorizations = @Authorization("OAuth"))
    public HttpEntity validateUserEmail (@PathVariable final String token) {
       return userService.validationUserEmail(token);
    }

    @GetMapping("/forget-password/request/{email}")
    @ApiOperation(value = "Envia um e-mail para o usuário cadastrado para confirmar a troca da senha",  authorizations = @Authorization("OAuth"))
    public HttpEntity forgetPasswordRequest (@PathVariable final String email) {
        return userService.userForgetPassword(email);
    }

    @PostMapping("/forget-password/confirm")
    @ApiOperation(value = "Redefine a senha do usuário cadastrado", notes = "Token codificado em Base64 URL",  authorizations = @Authorization("OAuth"))
    public HttpEntity forgetPasswordConfirm (@Valid @RequestBody final UserChangePassRequest userChangePassRequest) {
        return userService.userForgetPassword(userChangePassRequest);
    }

}
