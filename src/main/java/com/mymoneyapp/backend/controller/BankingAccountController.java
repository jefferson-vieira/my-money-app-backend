package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.model.Summary;
import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.response.BankingAccountResponse;
import com.mymoneyapp.backend.service.BankingAccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/banking-accounts")
public class BankingAccountController {

    @Autowired
    private BankingAccountService bankingAccountService;

    @PostMapping
    @ApiOperation(value = "Cadastra uma conta bancária para o usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@ApiIgnore @AuthenticationPrincipal final User user,
                           @Valid @RequestBody final BankingAccountRequest bankingAccountRequest) {
        Long bankingAccountId = bankingAccountService.save(user, bankingAccountRequest);
        return ResponseEntity.created(URI.create("/banking-accounts/" + bankingAccountId)).build();
    }

    @GetMapping
    @ApiOperation(value = "Lista as contas bancárias cadastradas do usuário", authorizations = @Authorization("OAuth"))
    public List<BankingAccountResponse> findAllByUser(@ApiIgnore @AuthenticationPrincipal final User user) {
        return bankingAccountService.findAllByUser(user);
    }

    @GetMapping("/summary")
    @ApiOperation(value = "Gera o sumário das contas", authorizations = @Authorization("OAuth"))
    public Summary getSummary(@ApiIgnore @AuthenticationPrincipal final User user) {
        return bankingAccountService.getSummary(user);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza os dados da conta bancária do usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity<?> update(@ApiIgnore @AuthenticationPrincipal final User user,
                                @PathVariable final Long id,
                                @Valid @RequestBody final BankingAccountRequest bankingAccountRequest) {
        bankingAccountService.update(user, id, bankingAccountRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove uma conta bancária do usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity<?> delete(@ApiIgnore @AuthenticationPrincipal final User user, @PathVariable final Long id) {
        bankingAccountService.delete(user, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/summary")
    @ApiOperation(value = "Gera o sumário de uma conta do usuário", authorizations = @Authorization("OAuth"))
    public Summary getSummary(@ApiIgnore @AuthenticationPrincipal final User user, @PathVariable final Long id) {
        return bankingAccountService.getSummary(user, id);
    }

}
