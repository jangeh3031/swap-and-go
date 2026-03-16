package com.swapandgo.sag.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleAddress {
    private String country;
    private String region;
    private String street;
}
