package com.mymoneyapp.backend.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserChangePassRequest {

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

    @NotEmpty(message = "O token é necessário para redefinir a senha")
    private String token;
}
