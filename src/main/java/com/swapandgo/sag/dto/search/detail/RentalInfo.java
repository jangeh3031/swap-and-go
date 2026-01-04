package com.swapandgo.sag.dto.search.detail;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RentalInfo {
    private Boolean isCurrentlyRented;
    private LocalDateTime rentedFrom;
    private LocalDateTime rentedUntil;
}
