package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.model.CardType;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    @NotBlank(message = "A descrição da transação precisa ser informado")
    private String description;

    @NotNull(message = "O valor da transação precisa ser informado")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Valor inválido")
    private Double value;

    @NotNull(message = "O número de parcelas precisa ser informado")
    @Digits(integer = 60, fraction = 0, message = "Número de parcelas inválido")
    @Min(1)
    private short installment;

    @NotNull(message = "O tipo da transação precisa ser informada")
    private CardType transactionType;

    @NotNull(message = "A data do crédito precisa ser informada")
    private LocalDate dateTransaction;

    @NotNull(message = "O cartão associada à essa transação precisa ser informada")
    private Long cardId;
}
