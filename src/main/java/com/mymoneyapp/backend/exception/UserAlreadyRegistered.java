package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Usuário ou e-mail já utilizado")
public class UserAlreadyRegistered extends RuntimeException {
}
