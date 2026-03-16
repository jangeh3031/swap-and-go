package com.swapandgo.sag.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String fullAddress;
    private Double latitude;
    private Double longitude;
    private String country;
    private String region;
    private String district;
    private String street;
    private String zipcode;

    public Address(){ }
    public Address(String fullAddress, Double latitude, Double longitude, String country,
                   String region, String district, String street, String zipcode){
        this.fullAddress = fullAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.region = region;
        this.district = district;
        this.street = street;
        this.zipcode = zipcode;
    }

    public Address(String country, String region, String street){
        this.country = country;
        this.region = region;
        this.street = street;
    }
}
