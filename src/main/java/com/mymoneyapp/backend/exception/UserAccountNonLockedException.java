package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Usuário já validou o e-mail da conta")
public class UserAccountNonLockedException extends RuntimeException {
}
