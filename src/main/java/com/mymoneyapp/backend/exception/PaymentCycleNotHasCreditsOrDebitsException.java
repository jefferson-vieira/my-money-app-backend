package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
        reason = "Um ciclo de pagamento deve ter pelo menos um crédito ou um débito vinculado")
public class PaymentCycleNotHasCreditsOrDebitsException extends RuntimeException {
}
