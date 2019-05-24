package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.validator.Password;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserChangePassRequest {

    @Password(message = "A senha é inválida")
    private String password;

    @NotBlank(message = "Confirmar a senha é necessário")
    private String confirmPassword;

    @NotBlank(message = "O token é necessário para redefinir a senha")
    private String token;

}
