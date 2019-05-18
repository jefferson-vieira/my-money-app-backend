package com.mymoneyapp.backend.response;

import com.mymoneyapp.backend.model.CardType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionResponse {

    private Long id;

    private CardType transactionType;

    private String description;

    private Double value;

    private short installment;

    private LocalDate dateTransaction;
}
