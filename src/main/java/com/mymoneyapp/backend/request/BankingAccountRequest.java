package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BankingAccountRequest {

    @NotBlank(message = "O nome da instituição bancária precisa ser informado")
    @Size(min = 4, max = 20, message = "O nome da instituição bancária precisa ter de 4 à 20 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no nome da instituição bancária")
    private String bankName;

    @NotBlank(message = "O número da agência bancária precisa ser informado")
    @Pattern(regexp = "^[\\d]*$", message = "Há caracteres inválidos no número da agência bancária")
    private String agency;

    @NotBlank(message = "O número da conta bancária precisa ser informado")
    @Pattern(regexp = "^[\\d]*$", message = "Há caracteres inválidos no número da conta bancária")
    private String number;

    @NotBlank(message = "O dígito da conta bancária precisa ser informado")
    @Size(min = 1, max = 1, message = "O dígito da conta bancária precisa ter um (1) caractere")
    @Pattern(regexp = "^[\\d]$", message = "Há caracteres inválidos no dígito da conta bancária")
    private String digit;

}
