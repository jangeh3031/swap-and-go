package com.swapandgo.sag.domain.user;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String fullAddress;
    private Double latitude;
    private Double longitude;
    private String country;
    private String city;
    private String district;
    private String street;
    private String zipcode;

    public Address(){ }
    public Address(String fullAddress, Double latitude, Double longitude, String country,
                   String city, String district, String street, String zipcode){
        this.fullAddress = fullAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.district = district;
        this.street = street;
        this.zipcode = zipcode;
    }
}
