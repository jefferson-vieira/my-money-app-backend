package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Não foi possivel enviar o e-mail para o usuário")
public class EmailCannotBeSent extends RuntimeException {
}
