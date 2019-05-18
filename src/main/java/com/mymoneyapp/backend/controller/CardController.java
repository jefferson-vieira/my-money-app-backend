package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.Card;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.CardRequest;
import com.mymoneyapp.backend.response.CardResponse;
import com.mymoneyapp.backend.service.CardService;
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
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    @ApiOperation(value = "Cadastra um cartão para o usuário", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@AuthenticationPrincipal final User user,
                                   @Valid @RequestBody final CardRequest cardRequest) {
        Long cardId = cardService.save(user, cardRequest);
        return ResponseEntity.created(URI.create("/cards/" + cardId)).build();
    }

    @GetMapping
    @ApiOperation(value = "Lista os cartões cadastrados do usuário", authorizations = @Authorization("OAuth"))
    public List<CardResponse> findAllByUser(@AuthenticationPrincipal final User user) {
        return cardService.findAllByUser(user);
    }
}
