package com.mymoneyapp.backend.controller;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import com.mymoneyapp.backend.service.PaymentCycleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@RestController
@RequestMapping("/payment-cycles")
public class PaymentCycleController {

    @Autowired
    private PaymentCycleService paymentCycleService;

    @GetMapping
    @ApiOperation(value = "Lista os ciclos de pagamentos cadastrados do usuário", authorizations = @Authorization("OAuth"))
    public Page<PaymentCycleResponse> findAll(@ApiIgnore @AuthenticationPrincipal final User user,
                                              @PageableDefault(sort = {"date", "description"}) final Pageable pageable) {
        return paymentCycleService.findAll(user, pageable);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Busca um ciclo de pagamento pelo ID", authorizations = @Authorization("OAuth"))
    public PaymentCycleResponse findById(@ApiIgnore @AuthenticationPrincipal final User user,
                                         @PathVariable final Long id) {
        return paymentCycleService.findById(user, id);
    }

    @PostMapping
    @ApiOperation(value = "Cadastra um ciclo de pagamento", authorizations = @Authorization("OAuth"))
    public HttpEntity save(@ApiIgnore @AuthenticationPrincipal final User user,
                           @Valid @RequestBody final PaymentCycleRequest paymentCycleRequest) {
        Long paymentCycleId = paymentCycleService.save(user, paymentCycleRequest);
        return ResponseEntity.created(URI.create("/payment-cycles/" + paymentCycleId)).build();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza os dados de um ciclo de pagamento", authorizations = @Authorization("OAuth"))
    public HttpEntity<?> update(@ApiIgnore @AuthenticationPrincipal final User user,
                                @PathVariable final Long id,
                                @Valid @RequestBody final PaymentCycleRequest paymentCycleRequest) {
        paymentCycleService.update(user, id, paymentCycleRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove um ciclo de pagamento", authorizations = @Authorization("OAuth"))
    public HttpEntity<?> delete(@ApiIgnore @AuthenticationPrincipal final User user, @PathVariable final Long id) {
        paymentCycleService.delete(user, id);
        return ResponseEntity.noContent().build();
    }

}
