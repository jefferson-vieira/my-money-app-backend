package com.mymoneyapp.backend.response;

import lombok.Data;

@Data
public class BankingAccountResponse {

    private Long id;

    private String bankName;

    private String agency;

    private String number;

    private String digit;

    private String createdAt;

}
