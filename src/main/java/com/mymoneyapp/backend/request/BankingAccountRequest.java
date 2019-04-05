package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BankingAccountRequest {

    @NotEmpty(message = "O número da conta bancária precisa ser informado")
    @Pattern.List({
            @Pattern(regexp = "^[0-9]*$", message = "Há caracteres inválidos no número da conta bancária"),
    })
    private String accountNumber;

    @NotEmpty(message = "O digito da conta bancária precisa ser informado")
    @Size(min = 1, max = 1, message = "O digito da conta bancária precisa ter 1 caractere")
    @Pattern.List({
            @Pattern(regexp = "[0-9]", message = "Há caracteres inválidos no digito da bancária"),
    })
    private String accountDigit;

    @NotEmpty(message = "O número da agência bancária precisa ser informado")
    @Pattern.List({
            @Pattern(regexp = "^[0-9]*$", message = "Há caracteres inválidos no número da agência bancária"),
    })
    private String accountAgency;

    @NotEmpty(message = "O nome da instituição bancária precisa ser informado")
    @Size(min = 4, max = 20, message = "O nome da instituição bancária precisa ter de 4 à 20 caracteres")
    @Pattern.List({
            @Pattern(regexp = "^[^\\s].*[^\\s]+$", message = "Nome da instituição bancária inválido"),
            @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no nome da instituição bancária"),
    })
    private String bankingInstitutionName;
}
