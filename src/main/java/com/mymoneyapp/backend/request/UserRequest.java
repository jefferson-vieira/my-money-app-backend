package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRequest {

    @NotBlank(message = "O nome precisa ser informado")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no nome")
    private String name;

    @NotBlank(message = "O e-mail precisa ser informado")
    @Email(message = "O formato do e-mail é inválido")
    private String email;

    @NotBlank(message = "A senha precisa ser informada")
    @Size(min = 8, max = 16, message = "A senha deve ter de 8 à 16 caracteres")
    @Pattern.List({
            @Pattern(regexp = "^[^\\s]+$", message = "A senha não pode conter espaços"),
            @Pattern(regexp = ".*\\W+.*", message = "A senha deve conter pelo menos um caractere especial"),
            @Pattern(regexp = ".*\\d+.*", message = "A senha deve conter pelo menos um número"),
            @Pattern(regexp = ".*[^\\W\\d]+.*", message = "A senha deve conter pelo menos uma letra"),
    })
    private String password;

    @NotEmpty(message = "Confirmar a senha é necessário")
    private String confirmPassword;

}
