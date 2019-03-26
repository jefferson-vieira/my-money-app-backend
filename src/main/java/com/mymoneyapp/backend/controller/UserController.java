package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.response.UserResponse;
import com.mymoneyapp.backend.service.UserService;
import com.mymoneyapp.backend.specification.UserSpecification;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "Lista os usuários cadastrados", authorizations = @Authorization("OAuth"))
    public Page<UserResponse> findAll(final UserSpecification userSpecification,
                                      @PageableDefault(sort = {"name"}) final Pageable pageable) {
        return userService.findAll(userSpecification, pageable);
    }

    @GetMapping("/me")
    @ApiOperation(value = "Retorna o usuário logado", authorizations = @Authorization("OAuth"))
    public User principal(@AuthenticationPrincipal final User user) {
        return user;
    }

}
