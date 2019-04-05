package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BankingAccountRequest {

    @NotEmpty(message = "O nome da instituição bancária precisa ser informado")
    @Size(min = 4, max = 20, message = "O nome da instituição bancária precisa ter de 4 à 20 caracteres")
    @Pattern.List({
            @Pattern(regexp = "^[^\\s].*[^\\s]+$", message = "Nome da instituição bancária inválido"),
            @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no nome da instituição bancária"),
    })
    private String bankName;

    @NotEmpty(message = "O número da agência bancária precisa ser informado")
    @Pattern.List({
            @Pattern(regexp = "^[0-9]*$", message = "Há caracteres inválidos no número da agência bancária"),
    })
    private String agency;

    @NotEmpty(message = "O número da conta bancária precisa ser informado")
    @Pattern.List({
            @Pattern(regexp = "^[0-9]*$", message = "Há caracteres inválidos no número da conta bancária"),
    })
    private String number;

    @NotEmpty(message = "O digito da conta bancária precisa ser informado")
    @Size(min = 1, max = 1, message = "O digito da conta bancária precisa ter 1 caractere")
    @Pattern.List({
            @Pattern(regexp = "[0-9]", message = "Há caracteres inválidos no digito da bancária"),
    })
    private String digit;

    @NotNull(message = "Informar o dono da conta é obrigatório")
    private Long userId;

}
