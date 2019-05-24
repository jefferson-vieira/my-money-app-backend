package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.validator.Alphabetic;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BankingAccountRequest {

    @Size(min = 4, max = 20, message = "O nome da instituição bancária precisa ter de 4 à 20 caracteres")
    @Alphabetic(message = "Há caracteres inválidos no nome da instituição bancária")
    private String bankName;

    @NotBlank(message = "O número da agência bancária precisa ser informado")
    @Pattern(regexp = "^[\\d]*$", message = "Há caracteres inválidos no número da agência bancária")
    private String agency;

    @NotBlank(message = "O número da conta bancária precisa ser informado")
    @Pattern(regexp = "^[\\d]*$", message = "Há caracteres inválidos no número da conta bancária")
    private String number;

    @Pattern(regexp = "^[\\d]$", message = "O dígito da conta bancária é inválido")
    private String digit;

}
