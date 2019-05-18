package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.model.CardType;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class CardRequest {

    @NotBlank(message = "O número do cartão precisa ser informado")
    @Size(min = 13, max = 20, message = "O número do cartão precisa ter de 13 a 19 caracteres")
    @Pattern(regexp = "^[\\d]*$", message = "Há caracteres inválidos no número do cartão")
    private String cardNumber;

    @NotBlank(message = "A bandeira do cartão precisa ser informado")
    @Size(max = 25, message = "A bandeira do cartão precisa ter 25 caracteres no maáximo")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos na bandeira do cartão ")
    private String CardFlag;

    @NotBlank(message = "O nome impresso no cartão precisa ser informado")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no nome impresso no cartão")
    private String cardOwnerName;

    @NotNull(message = "O código do cartão precisa ser informado")
    @Digits(integer = 999, fraction = 0,  message = "Valor inválido para o código")
    private short cardCode;

    @NotNull(message = "A data de vencimento precisa ser informada")
    private LocalDate cardExpiration;

    @NotNull(message = "O tipo de cartão precisa ser informada")
    private CardType cardType;

    @NotNull(message = "A conta bancária associada à esse cartão precisa ser informada")
    private Long bankingAccountId;
}
