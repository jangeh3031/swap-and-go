package com.swapandgo.sag.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class AuthTokens {
    private final String accessToken;
    private final String refreshToken;
    private final long accessTokenExpiresIn;
}