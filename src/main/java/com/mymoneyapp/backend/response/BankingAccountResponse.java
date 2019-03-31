package com.mymoneyapp.backend.response;

import lombok.Data;

@Data
public class BankingAccountResponse {

    //O que eu retorno para front fica nessa class
    private String accountNumber;

    private String accountDigit;

    private String accountAgency;

    private String bankingInstitutionName;
}
