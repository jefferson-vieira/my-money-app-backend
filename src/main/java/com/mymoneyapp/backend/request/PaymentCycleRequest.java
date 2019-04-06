package com.mymoneyapp.backend.request;

import com.mymoneyapp.backend.domain.Credit;
import com.mymoneyapp.backend.domain.Debit;
import com.mymoneyapp.backend.domain.PaymentCycleStatus;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class PaymentCycleRequest {

    @NotEmpty(message = "A descrição do ciclo de pagamento precisa ser informada")
    @Size(min = 4, max = 50, message = "A descrição do ciclo de pagamento precisa ter de 4 à 50 caracteres")
    @Pattern.List({
            @Pattern(regexp = "^[^\\s].*[^\\s]+$", message = "Descrição do ciclo de pagamento inválido"),
            @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "Há caracteres inválidos na descrição do ciclo de pagamento"),
    })
    private String description;

    @NotEmpty(message = "O valor precisa ser informado")
    @Digits(integer = 1, fraction = 2, message = "O valor do débito precisa ter duas casas decimais")
    private double value;

    @NotEmpty(message = "O status do débito precisa ser informado")
    private PaymentCycleStatus status;

    @NotEmpty(message = "A data precisa ser informado")
    private LocalDate data;

    @NotEmpty(message = "Algum crédito precisa ser informado")
    private List<Credit> credits;

    @NotEmpty(message = "Algum dédito precisa ser informado")
    private List<Debit> debits;
}
