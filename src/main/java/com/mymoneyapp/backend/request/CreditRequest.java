package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreditRequest {

    @NotBlank(message = "A descrição do crédito precisa ser informado")
    private String description;

    @NotNull(message = "O valor do crédito precisa ser informado")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Valor inválido para o crédito")
    private Double value;

    @NotNull(message = "A data do crédito precisa ser informada")
    private LocalDate date;

}

