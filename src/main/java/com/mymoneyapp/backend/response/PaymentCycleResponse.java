package com.mymoneyapp.backend.response;

import com.mymoneyapp.backend.domain.BankingAccount;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PaymentCycleResponse {

    private Long id;

    private BankingAccountResponse bankingAccount;

    private String description;

    private LocalDate date;

    private List<CreditResponse> credits;

    private List<DebitResponse> debits;

}
