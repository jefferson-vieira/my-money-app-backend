package com.mymoneyapp.backend.response;

import com.mymoneyapp.backend.model.CardType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CardResponse {

    private Long id;

    private String cardNumber;

    private String CardFlag;

    private String cardOwnerName;

    private short cardCode;

    private LocalDate cardExpiration;

    private CardType cardType;

    private List<TransactionResponse> transactions;
}
