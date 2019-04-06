package com.mymoneyapp.backend.response;

import com.mymoneyapp.backend.domain.Credit;
import com.mymoneyapp.backend.domain.Debit;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PaymentCycleResponse {

    private Long id;

    private String description;

    private LocalDate data;

    private List<Credit> credits;

    private List<Debit> debits;
}
