package com.swapandgo.sag.dto.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyProfileUpdateRequest {
    private String username;
    private String password;

    private String fullAddress;
    private Double latitude;
    private Double longitude;
    private String country;
    private String region;
    private String district;
    private String street;
    private String zipcode;
}
