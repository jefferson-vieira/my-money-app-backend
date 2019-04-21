package com.mymoneyapp.backend.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccessTokenResponse {

    private String token;

    private LocalDateTime expiresAt;
}
