package com.swapandgo.sag.dto.tradeoffer;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TradeOfferCreateRequest {
    @NotNull
    private Long itemId;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
