package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.response.BankingAccountResponse;
import com.mymoneyapp.backend.service.BankingAccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/banking-accounts")
public class BankingAccountController {

    @Autowired
    private BankingAccountService bankingAccountService;

    @PostMapping
    @ApiOperation(value = "Cadastra uma conta bancária", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@Valid @RequestBody final BankingAccountRequest bankingAccountRequest) {
        Long bankingAccountId = bankingAccountService.save(bankingAccountRequest);
        return ResponseEntity.created(URI.create("/banking-accounts/" + bankingAccountId)).build();
    }

    @GetMapping("/me")
    @ApiOperation(value = "Lista as contas bancárias cadastradas", authorizations = @Authorization("OAuth"))
    public List<BankingAccountResponse> findAllByUser(@AuthenticationPrincipal final User user) {
        return bankingAccountService.findAllByUser(user);
    }

}
