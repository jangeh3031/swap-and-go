package com.swapandgo.sag.dto.auth;


import com.swapandgo.sag.dto.user.SimpleAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String username;
    private SimpleAddress address;
}
