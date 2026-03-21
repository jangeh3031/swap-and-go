package com.swapandgo.sag.dto.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyProfileUpdateRequest {
    private String username;
    private String password;

    private String country;
    private String region;
    private String street;
}
