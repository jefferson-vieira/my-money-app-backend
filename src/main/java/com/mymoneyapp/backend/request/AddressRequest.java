package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.validator.Alphabetic;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AddressRequest {

    @NotBlank(message = "O CEP precisa ser informado")
    @Pattern(regexp = "^[\\d]{5}[-][\\d]{3}$", message = "Informe um CEP válido")
    private String postalCode;

    @Alphabetic(message = "Há caracteres inválidos no endereço")
    private String street;

    @NotBlank(message = "O número precisa ser informado")
    @Pattern(regexp = "^[\\d]+$", message = "O número é inválido")
    private String number;

    @Alphabetic(message = "Há caracteres inválidos no nome do bairro")
    private String district;

    @Alphabetic(message = "Há caracteres inválidos no nome da cidade")
    private String city;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Informe uma sigla válida")
    private String state;

}
