package com.mymoneyapp.backend.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PaymentCycleResponse {

    private Long id;

    private String description;

    private LocalDate date;

    private List<CreditResponse> credits;

    private List<DebitResponse> debits;

}
