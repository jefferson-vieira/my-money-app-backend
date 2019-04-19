package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Token do e-mail de verificação já expirou ou inválido")
public class EmailTokenHasExpiredException extends RuntimeException {
}
