package com.swapandgo.sag.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyCodeResponse {
    private String message;
    private String verificationToken;
}
