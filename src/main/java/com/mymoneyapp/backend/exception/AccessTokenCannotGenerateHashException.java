package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Não foi possivel gerar o hash do token")
public class AccessTokenCannotGenerateHashException extends RuntimeException{
}
