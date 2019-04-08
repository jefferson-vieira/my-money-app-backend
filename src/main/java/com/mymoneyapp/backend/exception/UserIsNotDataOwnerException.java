package com.mymoneyapp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "O usuário atual não é o dono destes dados")
public class UserIsNotDataOwnerException extends RuntimeException {
}

