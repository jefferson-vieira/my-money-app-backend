package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BankingAccountRequest {

    @NotEmpty(message = "O número da conta bancária precisa ser informado")//Não deixa o usuario digitar a conta do banco nulo
    @Digits(message = "Há caracteres inválidos no número da conta bancária")//Só deixa ele digitar numeros
    private String accountNumber;

    @NotEmpty(message = "O digito da conta da conta bancária precisa ser informado")
    @Digits(message = "Há caracteres inválidos no digito da conta bancária")
    private String accountDigit;

    @NotEmpty(message = "O digito da agência da agência bancária precisa ser informado")
    @Digits(message = "Há caracteres inválidos na agência da conta bancária")
    private String accountAgency;

    @NotEmpty(message = "O nome da instituição bancária precisa ser informado")
    @Size(min = 4, max = 20, message = "O nome da instituição bancária precisa deve ter de 4 à 20 caracteres")//limite o tamanho de caracteres da string
    @Pattern.List({
            @Pattern(regexp = "^[^\\s].*[^\\s]+$", message = "Nome da instituição bancária inválido"),
            @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no nome da instituição bancária"),
    })
    private String bankingInstitutionName;
}
