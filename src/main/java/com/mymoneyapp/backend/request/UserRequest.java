package com.mymoneyapp.backend.request;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UserRequest {

    @NotBlank(message = "O e-mail precisa ser informado")
    @Email(message = "O formato do e-mail é inválido")
    private String email;

    @NotEmpty(message = "Confirmar o e-mail é necessário")
    private String confirmEmail;

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

    @NotBlank(message = "O nome precisa ser informado")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no nome")
    private String name;

    @NotBlank(message = "O sobrenome precisa ser informado")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos no sobrenome")
    private String surname;

    @PastOrPresent(message = "A data de nascimento não pode ser no futuro")
    private LocalDate birthday;

    @NotBlank(message = "O CPF é obrigatório")
    @CPF(message = "O CPF é inválido")
    private String cpf;

    @NotBlank(message = "O número de telefone precisa ser informado")
    @Pattern(regexp = "^[\\d]{10}$", message = "Inform um número de telefone válido")
    private String telephone;

    @NotBlank(message = "O número de celular precisa ser informado")
    @Pattern(regexp = "^[\\d]{2}[9][\\d]{8}$", message = "Inform um número de celular válido")
    private String cellphone;

    @Valid
    private AddressRequest address;

}
