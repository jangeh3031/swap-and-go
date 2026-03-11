package com.swapandgo.sag.dto.trade;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TradeRequestCreateRequest {
    @NotNull
    private Long itemId;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
