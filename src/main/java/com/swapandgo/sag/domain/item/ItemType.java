package com.swapandgo.sag.domain.item;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemType {
    RESALE, RENTAL;


    //프론트 측에서 이미 소문자로 작성함
    @JsonValue
    public String toJson(){
        return name().toLowerCase();
    }
}


