package com.swapandgo.sag.dto.mypage;

import lombok.Getter;

@Getter
public class MyAddressResponse {
    private final String country;
    private final String region;
    private final String street;

    public MyAddressResponse(String country, String region, String street) {
        this.country = country;
        this.region = region;
        this.street = street;
    }
}
