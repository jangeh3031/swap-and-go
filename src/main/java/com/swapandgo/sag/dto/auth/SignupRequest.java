package com.swapandgo.sag.dto.auth;

import com.swapandgo.sag.domain.user.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Address address;
    private String verificationToken;
}
