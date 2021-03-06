package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.UserChangePassRequest;
import com.mymoneyapp.backend.request.UserRequest;
import com.mymoneyapp.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.ws.rs.PathParam;
import java.net.URI;

@RestController
@RequestMapping
public class OAuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @ApiOperation(value = "Retorna o usuário logado", authorizations = @Authorization("OAuth"))
    public User principal(@ApiIgnore @AuthenticationPrincipal final User user) {
        return user;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Cadastra um usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@Valid @RequestBody final UserRequest userRequest) {
        Long userId = userService.save(userRequest);
        return ResponseEntity.created(URI.create("/users/" + userId)).build();
    }

    @GetMapping("/registration-confirm/{token}")
    @ApiOperation(value = "Valída o e-mail do usuário cadastrado", authorizations = @Authorization("OAuth"))
    public HttpEntity userValidationEmail(@PathParam("token")
                                              @ApiParam(value = "Token gerado pelo sistema e enviado por e-mail ao usuário")
                                              @PathVariable final String token) {
        userService.activateUserAccount(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/registration-confirm/resend/{email}")
    @ApiOperation(value = "Reenvia o e-mail de confirmação para o usuário cadastrado", authorizations = @Authorization("OAuth"))
    public HttpEntity resendUserValidationEmail(@PathParam("email")
                                                    @ApiParam(value = "E-mail usado pelo usuário ao criar a conta")
                                                    @PathVariable final String email) {
        userService.resendUserValidationEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/forget-password/request/{email}")
    @ApiOperation(value = "Envia um e-mail para o usuário cadastrado para confirmar a troca da senha", authorizations = @Authorization("OAuth"))
    public HttpEntity forgetPasswordRequest(@PathParam("email")
                                                @ApiParam(value = "E-mail usado pelo usuário ao criar a conta")
                                                @PathVariable final String email) {
        userService.recoveryUserPassword(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/forget-password/confirm")
    @ApiOperation(value = "Redefine a senha do usuário cadastrado", authorizations = @Authorization("OAuth"))
    public HttpEntity forgetPasswordConfirm(@Valid @RequestBody final UserChangePassRequest userChangePassRequest) {
        userService.changeUserPassword(userChangePassRequest);
        return ResponseEntity.ok().build();
    }

}
