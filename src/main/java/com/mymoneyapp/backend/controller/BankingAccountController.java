package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.response.BankingAccountResponse;
import com.mymoneyapp.backend.service.BankingAccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
public class BankingAccountController {

    @Autowired
    private BankingAccountService bankingAccountService;

    @PostMapping("/createBankingAccount")
    @ApiOperation(value = "Cadastra uma conta bancária", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@Valid @RequestBody final BankingAccountRequest bankingAccountRequest) {
        Long bankingAccountId = bankingAccountService.save(bankingAccountRequest);
        return ResponseEntity.created(URI.create("/bankingAccounts/" + bankingAccountId)).build();
    }

    @GetMapping
    @ApiOperation(value = "Lista as contas bancárias cadastrados", authorizations = @Authorization("OAuth"))
    public List<BankingAccountResponse> findAll() {
        return bankingAccountService.findAll();
    }
}
