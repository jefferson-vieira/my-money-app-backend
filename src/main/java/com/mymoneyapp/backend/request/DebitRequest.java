package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.domain.DebitStatus;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class DebitRequest {

    @NotBlank(message = "A descrição do débito precisa ser informado")
    private String description;

    @NotNull(message = "O valor do débito precisa ser informado")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Valor inválido para o débito")
    private Double value;

    @NotNull(message = "A data do débito precisa ser informada")
    private LocalDate date;

    @NotNull(message = "O status do débito deve ser informado")
    private DebitStatus status;

}
