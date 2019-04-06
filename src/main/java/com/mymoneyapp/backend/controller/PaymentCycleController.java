package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import com.mymoneyapp.backend.service.PaymentCycleService;
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
@RequestMapping("/payment-cycles")
public class PaymentCycleController {

    @Autowired
    private PaymentCycleService paymentCycleService;

    @PostMapping
    @ApiOperation(value = "Cadastra um ciclo de pagamento", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@Valid @RequestBody final PaymentCycleRequest paymentCycleRequest) {
        Long paymentCyleId = paymentCycleService.save(paymentCycleRequest);
        return ResponseEntity.created(URI.create("/payment-cycles/" + paymentCyleId)).build();
    }

    @GetMapping("/me")
    @ApiOperation(value = "Lista os ciclos de pagamentos cadastrados", authorizations = @Authorization("OAuth"))
    public List<PaymentCycleResponse> findAllByBankingAccount(@AuthenticationPrincipal final BankingAccount bankingAccount) {
        return paymentCycleService.findAllByBankingAccount(bankingAccount);
    }

    /*@GetMapping("/summary")
    @ApiOperation(value = "Soma crédito e débito do ciclo de pagamentos cadastrado", authorizations = @Authorization("OAuth"))
    public double getSummary()
    {
        //cansei de tentar fazer
        return 0;
    }*/
}
