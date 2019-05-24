package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.validator.Alphabetic;
import com.mymoneyapp.backend.validator.Password;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UserRequest {

    @Email(message = "O formato do e-mail é inválido")
    private String email;

    @NotBlank(message = "Confirmar o e-mail é necessário")
    private String confirmEmail;

    @Password(message = "A senha é inválida")
    private String password;

    @NotBlank(message = "Confirmar a senha é necessário")
    private String confirmPassword;

    @Alphabetic(message = "Há caracteres inválidos no nome")
    private String name;

    @Alphabetic(message = "Há caracteres inválidos no sobrenome")
    private String surname;

    @PastOrPresent(message = "A data de nascimento não pode ser no futuro")
    private LocalDate birthday;

    @CPF(message = "O CPF é inválido")
    private String cpf;

    @Pattern(regexp = "^[\\d]{10}$", message = "Inform um número de telefone válido")
    private String telephone;

    @Pattern(regexp = "^[\\d]{2}[9][\\d]{8}$", message = "Inform um número de celular válido")
    private String cellphone;

    @Valid
    private AddressRequest address;

}
