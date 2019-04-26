package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Usuário não confirmou o e-mail da conta")
public class UserIsAccountLocked extends RuntimeException {
}
