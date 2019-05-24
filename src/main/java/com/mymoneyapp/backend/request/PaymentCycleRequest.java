package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class PaymentCycleRequest {

    @NotBlank(message = "A descrição do ciclo de pagamento precisa ser informada")
    private String description;

    @NotNull(message = "A data precisa ser informada")
    private LocalDate date;

    @Valid
    private List<CreditRequest> credits;

    @Valid
    private List<DebitRequest> debits;

    @NotNull(message = "A conta bancária precisa ser informada")
    private Long bankingAccount;

}
