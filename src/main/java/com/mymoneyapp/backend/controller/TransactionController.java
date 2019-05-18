package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.CardRequest;
import com.mymoneyapp.backend.request.TransactionRequest;
import com.mymoneyapp.backend.response.CardResponse;
import com.mymoneyapp.backend.response.TransactionResponse;
import com.mymoneyapp.backend.service.CardService;
import com.mymoneyapp.backend.service.TransactionService;
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
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transactios")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @ApiOperation(value = "Cadastra uma transação para o usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@AuthenticationPrincipal final User user,
                                   @Valid @RequestBody final TransactionRequest transactionRequest) {
        Long transactionId = transactionService.save(user, transactionRequest);
        return ResponseEntity.created(URI.create("/transactions/" + transactionId)).build();
    }

    @GetMapping("/card/{id}")
    @ApiOperation(value = "Lista as transações cadastrados do cartão", authorizations = @Authorization("OAuth"))
    public List<TransactionResponse> findAllByCaed(@PathParam("id") final Long id) {
        return transactionService.findAllByCardId(id);
    }
}
