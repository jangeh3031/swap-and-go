package com.swapandgo.sag.dto.mypage;

import com.swapandgo.sag.domain.user.Address;
import lombok.Getter;

@Getter
public class MyAddressResponse {
    private final String fullAddress;
    private final Double latitude;
    private final Double longitude;
    private final String country;
    private final String region;
    private final String district;
    private final String street;
    private final String zipcode;

    public MyAddressResponse(Address address) {
        if (address == null) {
            this.fullAddress = null;
            this.latitude = null;
            this.longitude = null;
            this.country = null;
            this.region = null;
            this.district = null;
            this.street = null;
            this.zipcode = null;
            return;
        }
        this.fullAddress = address.getFullAddress();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
        this.country = address.getCountry();
        this.region = address.getRegion();
        this.district = address.getDistrict();
        this.street = address.getStreet();
        this.zipcode = address.getZipcode();
    }
}
