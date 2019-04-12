package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import com.mymoneyapp.backend.service.PaymentCycleService;
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
@RequestMapping("/payment-cycles")
public class PaymentCycleController {

    @Autowired
    private PaymentCycleService paymentCycleService;

    @PostMapping
    @ApiOperation(value = "Cadastra um ciclo de pagamento", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@AuthenticationPrincipal final User user,
                           @Valid @RequestBody final PaymentCycleRequest paymentCycleRequest) {
        Long paymentCycleId = paymentCycleService.save(user, paymentCycleRequest);
        return ResponseEntity.created(URI.create("/payment-cycles/" + paymentCycleId)).build();
    }

    @GetMapping
    @ApiOperation(value = "Lista os ciclos de pagamentos cadastrados do usuário", authorizations = @Authorization("OAuth"))
    public List<PaymentCycleResponse> findAll(@AuthenticationPrincipal final User user) {
        return paymentCycleService.findAll(user);
    }

}
