package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Token já utilizado ou inválido")
public class AccessTokenWasUsedException extends RuntimeException {
}
