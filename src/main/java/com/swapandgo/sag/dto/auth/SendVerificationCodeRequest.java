package com.swapandgo.sag.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendVerificationCodeRequest {
    private String email;
}
