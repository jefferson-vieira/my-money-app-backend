package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Token do e-mail já utilizado ou inválido")
public class EmailTokenWasUsedException extends RuntimeException {
}
