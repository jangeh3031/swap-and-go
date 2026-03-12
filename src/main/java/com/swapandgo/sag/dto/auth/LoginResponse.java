package com.swapandgo.sag.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(staticName = "of")
public class LoginResponse {
    private String accessToken;
    private Long expiresIn;
}
